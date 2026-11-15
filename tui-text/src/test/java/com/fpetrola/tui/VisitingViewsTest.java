package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

class VisitingViewsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final ViewVisitor visitor = mock(ViewVisitor.class);

  @Test
  void aWindowHandsItselfToTheVisitorWithItsTitleAndArea() {
    Window window = toolkit.window("Saludo");
    Area area = toolkit.area(0, 0, 20, 5);
    window.place(area);

    window.accept(visitor);

    verify(visitor).visitWindow(window);
    assertEquals("Saludo", window.title());
    assertEquals(area, window.area());
  }


  @Test
  void aButtonHandsItselfToTheVisitorAndPressingItTellsItsListenersInOrder() {
    Button ok = toolkit.button("OK");
    ButtonListener first = mock(ButtonListener.class);
    ButtonListener second = mock(ButtonListener.class);
    ok.onPress(first);
    ok.onPress(second);

    ok.accept(visitor);
    ok.press();

    verify(visitor).visitButton(ok);
    assertEquals("OK", ok.caption());
    InOrder order = inOrder(first, second);
    order.verify(first).pressed(ok);
    order.verify(second).pressed(ok);
  }


  @Test
  void aLabelHandsItselfToTheVisitorAndShowsItsCurrentText() {
    Label message = toolkit.label("antes");
    message.text("después");

    message.accept(visitor);

    verify(visitor).visitLabel(message);
    assertEquals("después", message.text());
  }

  @Test
  void aWindowIsVisitedFirstThenEachPlacementRightBeforeItsView() {
    Window window = toolkit.window("Saludo");
    Point first = toolkit.point(1, 1);
    Point second = toolkit.point(1, 2);
    Button ok = toolkit.button("OK");
    Label hello = toolkit.label("hola");
    ArgumentCaptor<Placement> placements = ArgumentCaptor.captor();
    window.add(ok, first);
    window.add(hello, second);

    window.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).visitWindow(window);
    order.verify(visitor).visitPlacement(placements.capture());
    order.verify(visitor).visitButton(ok);
    order.verify(visitor).visitPlacement(placements.capture());
    order.verify(visitor).visitLabel(hello);
    assertEquals(List.of(first, second), placements.getAllValues().stream().map(Placement::position).toList());
  }


  @Test
  void aVisitorThatOnlyCaresAboutSomeViewsDoesNothingForTheRest() {
    Window window = toolkit.window("Saludo");
    Label hello = toolkit.label("hola");
    Button ok = toolkit.button("OK");
    List<View> seen = new ArrayList<>();
    window.add(hello, toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 1));

    window.accept(new ViewVisitor() {
      @Override
      public void visitButton(Button button) {
        seen.add(button);
      }
    });

    assertEquals(List.of(ok), seen);
  }




  @Test
  void aCheckBoxIsVisitedThenItsMarkWhichStartsUncheckedAndTogglingMakesChecked() {
    CheckBox remember = toolkit.checkBox("Recordar");

    remember.accept(visitor);
    remember.toggle();
    remember.accept(visitor);

    assertEquals("Recordar", remember.text());
    InOrder order = inOrder(visitor);
    order.verify(visitor).visitCheckBox(remember);
    order.verify(visitor).visitUnchecked(remember);
    order.verify(visitor).visitCheckBox(remember);
    order.verify(visitor).visitChecked(remember);
  }



  @Test
  void anInputLineHandsItselfToTheVisitorKeepsWhatIsTypedIntoItAndErasesTheLastOne() {
    InputLine name = toolkit.inputLine(10);

    name.accept(visitor);
    name.type('h');
    name.type('i');
    name.type('x');
    name.erase();

    verify(visitor).visitInputLine(name);
    assertEquals("hi", name.text());
    assertEquals(10, name.width());
  }

  @Test
  void aListBoxHandsItselfToTheVisitorAndWalksItsRowsWithTheSelectionAndScrollBar() {
    ListBox letters = toolkit.listBox(List.of("a", "bb", "c", "d"), 3);

    letters.previous();
    letters.next();
    letters.next();
    letters.next();
    letters.next();
    letters.accept(visitor);

    assertEquals(Optional.of("d"), letters.selected());
    assertEquals(4, letters.width());
    InOrder order = inOrder(visitor);
    order.verify(visitor).visitListBox(letters);
    order.verify(visitor).visitListItem(argThat(item -> item.text().equals("bb") && item.list() == letters));
    order.verify(visitor).visitScrollUp(letters);
    order.verify(visitor).visitListItem(argThat(item -> item.text().equals("c") && item.list() == letters));
    order.verify(visitor).visitScrollThumb(letters);
    ArgumentCaptor<ListItem> selected = ArgumentCaptor.captor();
    order.verify(visitor).visitSelectedListItem(selected.capture());
    assertEquals("d", selected.getValue().text());
    assertEquals(letters, selected.getValue().list());
    order.verify(visitor).visitScrollDown(letters);
  }



  @Test
  void aGroupOfRadioButtonsIsVisitedThenEachOptionAndItsChoiceStopsAtBothEnds() {
    RadioButtons group = toolkit.radioButtons(List.of("a", "bb", "c"));
    ArgumentCaptor<Option> chosen = ArgumentCaptor.captor();

    group.previous();
    group.next();
    group.next();
    group.next();
    group.accept(visitor);

    assertEquals(6, group.width());
    InOrder order = inOrder(visitor);
    order.verify(visitor).visitRadioButtons(group);
    order.verify(visitor).visitOption(argThat(option -> option.text().equals("a") && option.group() == group));
    order.verify(visitor).visitOption(argThat(option -> option.text().equals("bb") && option.group() == group));
    order.verify(visitor).visitChosenOption(chosen.capture());
    assertEquals("c", chosen.getValue().text());
    assertEquals(group, chosen.getValue().group());
  }



  @Test
  void aListBoxWalksRowsPastItsLastItemAsNoItemAndHasNoBarWhenEveryItemFits() {
    ListBox one = toolkit.listBox(List.of("a"), 3);

    one.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).visitSelectedListItem(argThat(item -> item.text().equals("a") && item.list() == one));
    order.verify(visitor).visitNoScrollBar(one);
    order.verify(visitor).visitNoListItem(one);
    order.verify(visitor).visitNoScrollBar(one);
    order.verify(visitor).visitNoListItem(one);
    order.verify(visitor).visitNoScrollBar(one);
  }

  @Test
  void aListBoxWalksTheTrackOfItsScrollBarAwayFromTheThumb() {
    ListBox five = toolkit.listBox(List.of("a", "b", "c", "d", "e"), 4);

    five.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).visitScrollUp(five);
    order.verify(visitor).visitScrollThumb(five);
    order.verify(visitor).visitScrollTrack(five);
    order.verify(visitor).visitScrollDown(five);
  }

  @Test
  void anItemOnARowOfAListSelectsItself() {
    ListBox letters = toolkit.listBox(List.of("a", "b", "c", "d", "e"), 3);
    List<ListItem> shown = new java.util.ArrayList<>();
    letters.next();
    letters.next();
    letters.next();
    letters.next();
    letters.accept(new ViewVisitor() {
      @Override
      public void visitListItem(ListItem item) {
        shown.add(item);
      }
    });

    shown.getFirst().select();

    assertEquals("c", shown.getFirst().text());
    assertEquals(Optional.of("c"), letters.selected());
  }

  @Test
  void anOptionOfAGroupChoosesItself() {
    RadioButtons group = toolkit.radioButtons(List.of("a", "b"));
    group.accept(new ViewVisitor() {
      @Override
      public void visitOption(Option option) {
        option.choose();
      }
    });
    group.accept(visitor);

    verify(visitor).visitOption(argThat(option -> option.text().equals("a")));
    verify(visitor).visitChosenOption(argThat(option -> option.text().equals("b")));
  }

  @Test
  void aMenuBarIsVisitedThenEachMenuFollowedByItsItemsAndAnItemRunsItsCommand() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Archivo");
    Menu help = toolkit.menu("Ayuda");
    Runnable create = mock(Runnable.class);
    MenuVisitor menus = mock(MenuVisitor.class);
    ArgumentCaptor<MenuItem> items = ArgumentCaptor.captor();
    file.add("Nuevo", create);
    file.add("Salir", () -> { });
    help.add("Acerca", () -> { });
    bar.add(file);
    bar.add(help);

    bar.accept(menus);

    InOrder order = inOrder(menus);
    order.verify(menus).visitMenuBar(bar);
    order.verify(menus).visitMenu(file);
    order.verify(menus, times(2)).visitMenuItem(items.capture());
    order.verify(menus).visitMenu(help);
    order.verify(menus).visitMenuItem(items.capture());
    assertEquals(List.of("Nuevo", "Salir", "Acerca"), items.getAllValues().stream().map(MenuItem::text).toList());
    assertEquals(List.of(file, file, help), items.getAllValues().stream().map(MenuItem::menu).toList());
    assertEquals("Archivo", file.title());
    assertEquals(9, file.width());
    items.getAllValues().getFirst().run();
    verify(create).run();
  }



  @Test
  void aLabelForAViewSaysWhichViewItIsLinkedToAndAPlainLabelToNone() {
    InputLine city = toolkit.inputLine(8);

    assertEquals(Optional.of(city), toolkit.label("Or~t~:", city).linked());
    assertEquals(Optional.empty(), toolkit.label("Hola").linked());
  }

  @Test
  void aDropDownIsVisitedThenWhileOpenEachItemWithTheHighlightedOneApart() {
    DropDown answer = toolkit.dropDown(List.of("Ja", "Nein"));
    ArgumentCaptor<DropDownItem> highlighted = ArgumentCaptor.captor();

    answer.accept(visitor);
    answer.down();
    answer.down();
    answer.up();
    answer.down();
    answer.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor, times(2)).visitDropDown(answer);
    order.verify(visitor).visitDropDownItem(argThat(item -> item.text().equals("Ja") && item.dropDown() == answer));
    order.verify(visitor).visitHighlightedDropDownItem(highlighted.capture());
    assertEquals("Nein", highlighted.getValue().text());
    assertEquals(4, answer.width());

    answer.close();
    assertEquals(Optional.of("Ja"), answer.chosen());
    answer.down();
    answer.down();
    answer.choose();
    assertEquals(Optional.of("Nein"), answer.chosen());
  }
}
