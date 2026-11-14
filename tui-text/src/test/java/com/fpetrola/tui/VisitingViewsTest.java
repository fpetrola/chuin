package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
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
  void aWindowWalksItsViewsInTheOrderTheyWereAddedWithTheirPositions() {
    Window window = toolkit.window("Saludo");
    Point first = toolkit.point(1, 1);
    Point second = toolkit.point(1, 2);
    Button ok = toolkit.button("OK");
    Label hello = toolkit.label("hola");
    ChildVisitor children = mock(ChildVisitor.class);
    window.add(ok, first);
    window.add(hello, second);

    window.acceptChildren(children);

    InOrder order = inOrder(children);
    order.verify(children).child(ok, first);
    order.verify(children).child(hello, second);
  }

  @Test
  void aWindowHasAVisitorVisitEachViewPlacedInItButOnlyWhenAsked() {
    Window window = toolkit.window("Saludo");
    Label hello = toolkit.label("hola");
    Button ok = toolkit.button("OK");
    List<View> seen = new ArrayList<>();
    window.add(hello, toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 1));
    ViewVisitor collecting = new ViewVisitor() {
      @Override
      public void visitLabel(Label label) {
        seen.add(label);
      }

      @Override
      public void visitButton(Button button) {
        seen.add(button);
      }
    };

    window.accept(collecting);
    assertEquals(List.of(), seen);

    window.acceptViews(collecting);
    assertEquals(List.of(hello, ok), seen);
  }



  @Test
  void aCheckBoxHandsItselfToTheVisitorStartsUncheckedAndTogglingItMakesItChecked() {
    CheckBox remember = toolkit.checkBox("Recordar");
    MarkVisitor marks = mock(MarkVisitor.class);

    remember.accept(visitor);
    remember.acceptMark(marks);
    remember.toggle();
    remember.acceptMark(marks);

    verify(visitor).visitCheckBox(remember);
    assertEquals("Recordar", remember.text());
    InOrder order = inOrder(marks);
    order.verify(marks).unchecked();
    order.verify(marks).checked();
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
    ListRowVisitor rows = mock(ListRowVisitor.class);

    letters.accept(visitor);
    letters.previous();
    letters.next();
    letters.next();
    letters.next();
    letters.next();
    letters.acceptRows(rows);

    verify(visitor).visitListBox(letters);
    assertEquals(Optional.of("d"), letters.selected());
    assertEquals(4, letters.width());
    InOrder order = inOrder(rows);
    order.verify(rows).item("bb");
    order.verify(rows).barUp();
    order.verify(rows).item("c");
    order.verify(rows).barThumb();
    order.verify(rows).selectedItem("d");
    order.verify(rows).barDown();
  }



  @Test
  void aGroupOfRadioButtonsHandsItselfToTheVisitorWalksItsOptionsAndItsChoiceStopsAtBothEnds() {
    RadioButtons group = toolkit.radioButtons(List.of("a", "bb", "c"));
    OptionVisitor options = mock(OptionVisitor.class);

    group.accept(visitor);
    group.previous();
    group.next();
    group.next();
    group.next();
    group.acceptOptions(options);

    verify(visitor).visitRadioButtons(group);
    assertEquals(6, group.width());
    InOrder order = inOrder(options);
    order.verify(options).option("a");
    order.verify(options).option("bb");
    order.verify(options).chosenOption("c");
  }


  @Test
  void aListBoxWalksRowsPastItsLastItemAsNoItemAndHasNoBarWhenEveryItemFits() {
    ListBox one = toolkit.listBox(List.of("a"), 3);
    ListRowVisitor rows = mock(ListRowVisitor.class);

    one.acceptRows(rows);

    InOrder order = inOrder(rows);
    order.verify(rows).selectedItem("a");
    order.verify(rows).noBar();
    order.verify(rows).noItem();
    order.verify(rows).noBar();
    order.verify(rows).noItem();
    order.verify(rows).noBar();
  }

  @Test
  void aListBoxWalksTheTrackOfItsScrollBarAwayFromTheThumb() {
    ListBox five = toolkit.listBox(List.of("a", "b", "c", "d", "e"), 4);
    ListRowVisitor rows = mock(ListRowVisitor.class);

    five.acceptRows(rows);

    InOrder order = inOrder(rows);
    order.verify(rows).barUp();
    order.verify(rows).barThumb();
    order.verify(rows).barTrack();
    order.verify(rows).barDown();
  }

  @Test
  void aListBoxSelectsTheItemOnARowCountedFromTheFirstRowShown() {
    ListBox letters = toolkit.listBox(List.of("a", "b", "c", "d", "e"), 3);
    letters.next();
    letters.next();
    letters.next();
    letters.next();

    letters.selectShown(0);
    assertEquals(Optional.of("c"), letters.selected());

    letters.selectShown(3);
    letters.selectShown(-1);
    assertEquals(Optional.of("c"), letters.selected());
  }

  @Test
  void aGroupOfRadioButtonsChoosesAnOptionByItsPlaceAndIgnoresPlacesItDoesNotHave() {
    RadioButtons group = toolkit.radioButtons(List.of("a", "b"));
    OptionVisitor options = mock(OptionVisitor.class);

    group.choose(1);
    group.choose(5);
    group.choose(-1);
    group.acceptOptions(options);

    verify(options).option("a");
    verify(options).chosenOption("b");
  }

  @Test
  void aMenuBarWalksItsMenusLeftToRightAndEachKnowsItsTitle() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Archivo");
    Menu help = toolkit.menu("Ayuda");
    MenuVisitor menus = mock(MenuVisitor.class);
    bar.add(file);
    bar.add(help);

    bar.acceptMenus(menus);

    InOrder order = inOrder(menus);
    order.verify(menus).visitMenu(file);
    order.verify(menus).visitMenu(help);
    assertEquals("Archivo", file.title());
  }
}
