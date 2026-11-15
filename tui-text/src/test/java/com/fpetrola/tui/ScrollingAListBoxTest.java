package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScrollingAListBoxTest {
  private static final String DOWN = "\u001b[B";
  private static final String UP = "\u001b[A";

  @Test
  void theArrowsMoveTheSelectionOfAFocusedListAndItScrollsToKeepItInSight() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 7);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("L");
    ListBox numbers = toolkit.listBox(List.of("uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho"), 5);
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream((DOWN.repeat(6) + UP).getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 12, 7));
    window.add(numbers, toolkit.point(1, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals(List.of(
        "╔═══ L ════╗  ",
        "║ tres   ▲ ║  ",
        "║ cuatro ░ ║  ",
        "║ cinco  ■ ║  ",
        "║ seis   ░ ║  ",
        "║ siete  ▼ ║  ",
        "╚══════════╝  "), screen.lines());
    assertEquals("wwffffffwwwws.", screen.tints().get(4));
  }

  @Test
  void theArrowsStillMoveTheWindowWhenTheFocusIsNotOnAList() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(10, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("B");
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream(DOWN.getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 8, 3));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("╔═ B ══╗  ", screen.lines().get(1));
  }

  @Test
  void aListOnlyTakesTheArrowsAndTheFocusTintAfterTabReachesIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 8);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("C");

    window.place(toolkit.area(0, 0, 12, 7));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(toolkit.listBox(List.of("a", "b", "c", "d", "e"), 3), toolkit.point(0, 1));
    desktop.open(window);
    desktop.type(visitor -> visitor.down());
    desktop.draw(screen);

    assertEquals("╔═══ C ════╗  ", screen.lines().get(1));
    assertEquals(List.of("wwwwwwwwwwwws.", "wwwwwwwwwwwws.", "wwwwwwwwwwwws."), screen.tints().subList(3, 6));

    desktop.type(toolkit.tabKey());
    desktop.type(visitor -> visitor.down());
    desktop.draw(screen);

    assertEquals("╔═══ C ════╗  ", screen.lines().get(1));
    assertEquals(List.of("wwwwwwwwwwwws.", "wfwwwwwwwwwws.", "wwwwwwwwwwwws."), screen.tints().subList(3, 6));
  }

  @Test
  void aListWithFewerItemsThanRowsShowsBlankRowsAndNoScrollBar() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 6);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 10, 5));
    window.add(toolkit.listBox(List.of("solo"), 3), toolkit.point(1, 0));
    desktop.open(window);
    desktop.type(visitor -> visitor.down());
    desktop.draw(screen);

    assertEquals(List.of(
        "║ solo   ║  ",
        "║        ║  ",
        "║        ║  "), screen.lines().subList(1, 4));
    assertEquals("wwffffwwwws.", screen.tints().get(1));
  }

  @Test
  void anEmptyListShowsBlankRowsAndTheArrowsDoNothing() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 6);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 10, 5));
    window.add(toolkit.listBox(List.of(), 3), toolkit.point(1, 0));
    desktop.open(window);
    desktop.type(visitor -> visitor.down());
    desktop.type(visitor -> visitor.up());
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ A ═══╗  ",
        "║        ║  ",
        "║        ║  ",
        "║        ║  "), screen.lines().subList(0, 4));
    assertEquals("wwwwwwwwwws.", screen.tints().get(1));
  }

  @Test
  void aListNeedsThreeRowsForItsScrollBar() {
    Toolkit toolkit = new TextToolkit();

    assertThrows(IllegalArgumentException.class, () -> toolkit.listBox(List.of("a", "b", "c"), 2));
    assertThrows(IllegalArgumentException.class, () -> toolkit.listBox(List.of("a"), 1));
  }

  @Test
  void aListCountsAsAStopOnTheWayToTheViewsAfterIt() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Label said = toolkit.label("");
    Button ok = toolkit.button("Ok");
    Screen screen = toolkit.screen(20, 8);

    window.place(toolkit.area(0, 0, 20, 8));
    window.add(toolkit.listBox(List.of("a", "b"), 3), toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 3));
    window.add(said, toolkit.point(0, 4));
    ok.onPress(pressed -> said.text("ok"));
    desktop.open(window);
    desktop.type(toolkit.enterKey());
    desktop.type(toolkit.tabKey());
    desktop.draw(screen);
    assertEquals("║ ▀▀▀▀▀▀           ║", screen.lines().get(5));

    desktop.type(toolkit.enterKey());
    desktop.draw(screen);
    assertEquals("║ok▀▀▀▀▀           ║", screen.lines().get(5));
  }

  /** The list sits in a window at the top left, its scroll bar in column 9 from row 1 (▲) to row 5 (▼). */
  private static ListBox eightInFive(Toolkit toolkit, Desktop desktop) {
    Window window = toolkit.window("L");
    ListBox numbers = toolkit.listBox(List.of("uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho"), 5);
    window.place(toolkit.area(0, 0, 12, 7));
    window.add(numbers, toolkit.point(1, 0));
    desktop.open(window);
    desktop.draw(toolkit.screen(14, 7));
    return numbers;
  }

  @Test
  void clickingTheArrowsOfTheScrollBarSelectsTheItemAfterOrBefore() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    ListBox numbers = eightInFive(toolkit, desktop);

    desktop.click(toolkit.point(9, 5));
    desktop.click(toolkit.point(9, 5));
    desktop.click(toolkit.point(9, 5));
    assertEquals("cuatro", numbers.selected().orElseThrow());

    desktop.click(toolkit.point(9, 1));
    assertEquals("tres", numbers.selected().orElseThrow());
  }

  @Test
  void clickingTheTrackBelowOrAboveTheThumbMovesTheSelectionAPage() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    ListBox numbers = eightInFive(toolkit, desktop);

    desktop.click(toolkit.point(9, 4));
    assertEquals("cinco", numbers.selected().orElseThrow());

    desktop.click(toolkit.point(9, 2));
    assertEquals("uno", numbers.selected().orElseThrow());
  }

  @Test
  void clickingAnItemBesideTheScrollBarStillSelectsIt() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    ListBox numbers = eightInFive(toolkit, desktop);

    desktop.click(toolkit.point(8, 3));
    assertEquals("tres", numbers.selected().orElseThrow());
  }

  @Test
  void aPageIsOneFewerThanTheRowsShownAndStopsAtBothEnds() {
    ListBox numbers = new TextToolkit().listBox(List.of("uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho"), 5);

    numbers.pageUp();
    assertEquals("uno", numbers.selected().orElseThrow());
    numbers.pageDown();
    assertEquals("cinco", numbers.selected().orElseThrow());
    numbers.pageDown();
    assertEquals("ocho", numbers.selected().orElseThrow());
    numbers.pageDown();
    assertEquals("ocho", numbers.selected().orElseThrow());
    numbers.pageUp();
    assertEquals("cuatro", numbers.selected().orElseThrow());
  }

  /**
   * Eight items in five rows: ▲ on row 1, the track on rows 2 to 4, ▼ on row 5. Pressing on the thumb, on row 2 while
   * the first item is selected, and dragging it to row 4 selects the last item; after the release a drag does nothing.
   */
  @Test
  void draggingTheThumbOfAListSelectsTheItemAsFarAlong() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    ListBox numbers = eightInFive(toolkit, desktop);

    desktop.click(toolkit.point(9, 2));
    desktop.drag(toolkit.point(9, 4));
    assertEquals("ocho", numbers.selected().orElseThrow());

    desktop.drag(toolkit.point(9, 3));
    desktop.release(toolkit.point(9, 3));
    desktop.drag(toolkit.point(9, 2));
    assertEquals("cinco", numbers.selected().orElseThrow());
  }
}
