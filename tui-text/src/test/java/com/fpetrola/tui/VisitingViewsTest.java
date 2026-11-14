package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class VisitingViewsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final ViewVisitor visitor = mock(ViewVisitor.class);

  @Test
  void aWindowHandsItsTitleAreaAndPlacedButtonsToTheVisitor() {
    Window window = toolkit.window("Saludo");
    Area area = toolkit.area(0, 0, 20, 5);
    Point position = toolkit.point(1, 1);
    Button ok = toolkit.button("OK");
    View view = ok;
    window.place(area);
    window.add(view, position);

    window.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).window("Saludo", area);
    order.verify(visitor).child(position);
    order.verify(visitor).button(ok, "OK", List.of());
  }

  @Test
  void aButtonHandsItselfItsCaptionAndItsListenersToTheVisitor() {
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);
    ok.onPress(listener);

    ok.accept(visitor);

    verify(visitor).button(ok, "OK", List.of(listener));
  }

  @Test
  void aLabelHandsItsCurrentTextToTheVisitor() {
    Label message = toolkit.label("antes");
    message.text("después");

    message.accept(visitor);

    verify(visitor).label("después");
  }

  @Test
  void aWindowHandsItsPlacedLabelsToTheVisitor() {
    Window window = toolkit.window("Saludo");
    Point position = toolkit.point(1, 2);
    window.add(toolkit.label("hola"), position);

    window.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).child(position);
    order.verify(visitor).label("hola");
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
}
