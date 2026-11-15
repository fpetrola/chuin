package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.ScrollPanel;
import com.fpetrola.chuin.view.ViewVisitor;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * A window smaller than what it holds shows part of it, from the top left; the focus going to a view whose first cell
 * is out of sight scrolls the window just enough to show it.
 */
class ScrollingAWindowTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(10, 6);
  private final Desktop desktop = toolkit.desktop();
  private final Window window = toolkit.window("W");

  ScrollingAWindowTest() {
    window.place(toolkit.area(0, 0, 10, 5));
    window.add(toolkit.inputLine(3), toolkit.point(5, 0));
    for (int line = 0; line < 6; line++) {
      window.add(toolkit.label("l " + line), toolkit.point(0, line));
    }
    window.add(toolkit.inputLine(3), toolkit.point(0, 6));
    desktop.open(window);
    desktop.draw(screen);
  }

  @Test
  void aWindowShowsWhatFitsInsideItsFrameFromTheTop() {
    assertEquals(List.of(
        "╔══ W ═══╗",
        "║l 0     ▲",
        "║l 1     ■",
        "║l 2     ▼",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusOnAViewOutOfSightScrollsTheWindowJustEnoughToShowIt() {
    type("\tok");
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ W ═══╗",
        "║l 4     ▲",
        "║l 5     ■",
        "║ok      ▼",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusGoingBackToAViewAboveScrollsTheWindowBackUp() {
    type("\t");
    desktop.draw(screen);
    type("\t");
    desktop.draw(screen);

    assertEquals("║l 0     ▲", screen.lines().get(1));
  }

  @Test
  void aWindowScrollsFromItsOriginNeitherBeforeTheTopLeftNorPastWhatItHolds() {
    panelOf(window).scroll(2, 3);
    panelOf(window).scroll(-5, -1);

    assertEquals(toolkit.point(0, 2), panelOf(window).origin());
  }

  /** Seven rows held in five: the right side of the frame carries ▲, a track with the thumb where the origin is, and ▼. */
  @Test
  void aWindowThatHoldsMoreRowsThanItShowsHasAScrollBarOnItsRightSide() {
    Screen tall = toolkit.screen(10, 8);
    window.place(toolkit.area(0, 0, 10, 7));
    desktop.draw(tall);

    assertEquals("▲■░░▼", rightSide(tall));

    type("\t");
    desktop.draw(tall);

    assertEquals("▲░░■▼", rightSide(tall));
    assertEquals("╚════════╝", tall.lines().get(6));
  }

  @Test
  void aWindowThatShowsAllItHoldsHasNoScrollBar() {
    Screen tall = toolkit.screen(10, 9);
    window.place(toolkit.area(0, 0, 10, 9));
    desktop.draw(tall);

    assertEquals("║║║║║║║", rightSide(tall));
  }

  /** Seven rows in five: the window scrolls at most two. ▼ and ▲ move one row; the track, a page, as far as it goes. */
  @Test
  void clickingTheArrowsAndTheTrackOfTheRightBarScrollsTheWindow() {
    Screen tall = toolkit.screen(10, 8);
    window.place(toolkit.area(0, 0, 10, 7));
    desktop.draw(tall);

    desktop.click(toolkit.point(9, 5));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 1), panelOf(window).origin());

    desktop.click(toolkit.point(9, 1));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 0), panelOf(window).origin());

    desktop.click(toolkit.point(9, 4));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 2), panelOf(window).origin());
    assertEquals("▲░░■▼", rightSide(tall));

    desktop.click(toolkit.point(9, 5));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 2), panelOf(window).origin());

    desktop.click(toolkit.point(9, 2));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 0), panelOf(window).origin());
  }

  @Test
  void clickingTheRightArrowOfTheBottomBarScrollsTheWindowAColumn() {
    window.add(toolkit.label("una linea larga"), toolkit.point(0, 1));
    desktop.draw(screen);

    desktop.click(toolkit.point(8, 4));
    desktop.draw(screen);

    assertEquals(toolkit.point(1, 0), panelOf(window).origin());
  }

  @Test
  void aClickOnTheFrameOfAWindowThatShowsAllItHoldsScrollsNothing() {
    Screen tall = toolkit.screen(10, 9);
    window.place(toolkit.area(0, 0, 10, 9));
    desktop.draw(tall);

    desktop.click(toolkit.point(9, 7));

    assertEquals(toolkit.point(0, 0), panelOf(window).origin());
  }

  /** A button's shadow falls past it; with the button on the last row and column of the inside, everything still fits. */
  @Test
  void aButtonWhoseShadowFallsOnTheFrameGivesNoScrollBar() {
    Toolkit own = new TextToolkit();
    Desktop alone = own.desktop();
    Window snug = own.window("B");
    Screen small = own.screen(10, 4);
    snug.place(own.area(0, 0, 8, 3));
    snug.add(own.button("Ok"), own.point(0, 0));
    alone.open(snug);
    alone.draw(small);

    assertEquals(List.of(
        "╔═ B ══╗  ",
        "║  Ok  ║  ",
        "╚══════╝  "), small.lines().subList(0, 3));
  }

  /**
   * A panel of any view: five columns by three, holding a list four rows high, shows two rows and a column less than
   * it is, its bars taking its last column and row; ▼ scrolls it a row, and the bar beside a click on the inside lets it
   * go otherwise.
   */
  @Test
  void aPanelOfAnyViewShowsPartOfItAndItsBarsScrollIt() {
    ListBox numbers = toolkit.listBox(List.of("uno", "dos", "tres", "cuatro"), 4);
    ScrollPanel panel = toolkit.scrollPanel(numbers, 5, 3);
    StringBuilder otherwise = new StringBuilder();
    ClickTarget down = new BarTarget(panel.vertical(), new TextBarLine(toolkit.point(4, 0), toolkit.point(0, 1)), 2, held -> { });

    down.click(toolkit.point(2, 1), () -> otherwise.append("inside "));
    down.click(toolkit.point(4, 1), () -> otherwise.append("bar "));
    panel.scroll(0, 5);

    assertEquals("5x3", panel.size().columns() + "x" + panel.size().rows());
    assertEquals("inside ", otherwise.toString());
    assertEquals(toolkit.point(0, 2), panel.origin());
  }

  /** A panel placed in a window draws part of what it holds and its bars in its last column and row. */
  @Test
  void aPanelPlacedInAWindowDrawsPartOfWhatItHoldsAndItsBars() {
    Toolkit own = new TextToolkit();
    Desktop alone = own.desktop();
    Screen small = own.screen(12, 6);
    Window holder = own.window("P");
    holder.place(own.area(0, 0, 12, 6));
    holder.add(own.scrollPanel(own.listBox(List.of("uno", "dos", "tres", "cuatro"), 4), 5, 3), own.point(0, 0));
    alone.open(holder);
    alone.draw(small);

    assertEquals(List.of(
        "║uno ▲     ║",
        "║dos ▼     ║",
        "║◄■░►      ║"), small.lines().subList(1, 4));
  }

  /** A panel is entered, what it holds is visited, and it is left. */
  @Test
  void aPanelIsEnteredThenWhatItHoldsThenLeft() {
    Label held = toolkit.label("x");
    ScrollPanel panel = toolkit.scrollPanel(held, 3, 3);
    StringBuilder met = new StringBuilder();

    panel.accept(new ViewVisitor() {
      @Override
      public void visitScrollPanel(ScrollPanel visited) {
        met.append("enter ");
      }

      @Override
      public void visitLabel(Label visited) {
        met.append("label ");
      }

      @Override
      public void leaveScrollPanel(ScrollPanel visited) {
        met.append("leave");
      }
    });

    assertEquals("enter label leave", met.toString());
  }

  /** The bar across a panel's last row scrolls it by columns, a page being the columns shown less one, as far as it goes. */
  @Test
  void theHorizontalBarOfAPanelScrollsItByColumns() {
    ScrollPanel panel = toolkit.scrollPanel(toolkit.label("una linea larga"), 6, 2);

    panel.horizontal().next();
    panel.horizontal().pageDown();
    panel.horizontal().pageDown();
    assertEquals(toolkit.point(9, 0), panel.origin());

    panel.horizontal().pageDown();
    assertEquals(toolkit.point(10, 0), panel.origin());
  }

  /** A group keeps the views placed in it, visits them in the order added, and reaches as far as its farthest view. */
  @Test
  void aGroupVisitsItsViewsInOrderAndIsAsBigAsItsFarthestView() {
    Group group = new TextGroup();
    StringBuilder met = new StringBuilder();
    group.add(toolkit.label("uno"), toolkit.point(4, 0));
    group.add(toolkit.label("dos"), toolkit.point(0, 2));

    group.accept(new ViewVisitor() {
      @Override
      public void visitLabel(Label label) {
        met.append(label.text()).append(' ');
      }
    });

    assertEquals("uno dos ", met.toString());
    assertEquals("7x3", group.size().columns() + "x" + group.size().rows());
  }

  /** A view out of sight takes no click, not even where it would be if the window showed it. */
  @Test
  void aClickWhereAViewOutOfSightWouldBePressesNothing() {
    Toolkit own = new TextToolkit();
    Desktop alone = own.desktop();
    Screen tall = own.screen(12, 10);
    Window held = own.window("H");
    Button hidden = own.button("No");
    Label said = own.label("");
    held.place(own.area(0, 0, 12, 5));
    held.add(own.button("Ok"), own.point(0, 0));
    held.add(said, own.point(0, 1));
    held.add(hidden, own.point(0, 5));
    hidden.onPress(pressed -> said.text("no"));
    alone.open(held);
    alone.draw(tall);

    alone.click(own.point(3, 6));
    alone.release(own.point(3, 6));

    assertEquals("", said.text());
  }

  /**
   * Seven rows held in five, so the window scrolls at most two and the thumb walks three cells of track, rows 2 to 4
   * of the screen: pressing on the thumb and dragging it to the last track cell scrolls to the end, and back to the first.
   */
  @Test
  void draggingTheThumbOfTheRightBarScrollsTheWindowAsFarAlong() {
    Screen tall = toolkit.screen(10, 8);
    window.place(toolkit.area(0, 0, 10, 7));
    desktop.draw(tall);

    desktop.click(toolkit.point(9, 2));
    desktop.drag(toolkit.point(9, 4));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 2), panelOf(window).origin());
    assertEquals("▲░░■▼", rightSide(tall));

    desktop.drag(toolkit.point(9, 2));
    desktop.release(toolkit.point(9, 2));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 0), panelOf(window).origin());

    desktop.drag(toolkit.point(9, 4));
    assertEquals(toolkit.point(0, 0), panelOf(window).origin());
  }

  /** A press on a bar's thumb hands a drag to whoever follows the pointer, which puts the thumb under it at each move. */
  @Test
  void aPressOnTheThumbHandsADragThatPutsTheThumbUnderThePointer() {
    ListBox numbers = toolkit.listBox(List.of("a", "b", "c", "d", "e", "f", "g", "h"), 8);
    ScrollPanel panel = toolkit.scrollPanel(numbers, 3, 6);
    List<Dragging> held = new java.util.ArrayList<>();
    Grab grab = held::add;
    ClickTarget down = new BarTarget(panel.vertical(), new TextBarLine(toolkit.point(2, 0), toolkit.point(0, 1)), 5, grab);

    down.click(toolkit.point(2, 1), () -> { });
    held.getFirst().to(toolkit.point(2, 2));
    assertEquals(toolkit.point(0, 2), panel.origin());

    held.getFirst().to(toolkit.point(7, 9));
    assertEquals(toolkit.point(0, 3), panel.origin());
  }

  /** A grab holds the drag it is given, which is done each time the pointer moves. */
  @Test
  void aGrabHoldsADragThatIsDoneAtEachMove() {
    List<Dragging> held = new java.util.ArrayList<>();
    List<Point> moves = new java.util.ArrayList<>();
    Grab grab = held::add;

    grab.hold(moves::add);
    held.getFirst().to(toolkit.point(1, 2));
    held.getFirst().to(toolkit.point(3, 4));

    assertEquals(List.of(toolkit.point(1, 2), toolkit.point(3, 4)), moves);
  }

  /** The scroll panel that is the inside of a window. */
  private static ScrollPanel panelOf(Window window) {
    List<ScrollPanel> panels = new java.util.ArrayList<>();
    window.accept(new ViewVisitor() {
      @Override
      public void visitScrollPanel(ScrollPanel panel) {
        panels.add(panel);
      }
    });
    return panels.getFirst();
  }

  /** The cells of the window's right side between its corners. */
  private static String rightSide(Screen screen) {
    StringBuilder side = new StringBuilder();
    List<String> lines = screen.lines();
    for (int row = 1; row < lines.size() && lines.get(row).charAt(9) != '╝'; row++) {
      side.append(lines.get(row).charAt(9));
    }
    return side.toString();
  }

  private void type(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
