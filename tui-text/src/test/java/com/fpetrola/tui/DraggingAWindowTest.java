package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Terminals report a drag as presses of the left button (ESC [ < 0 ; x ; y M), moves with it held (ESC [ < 32 ; x ; y M)
 * and a release (ESC [ < 0 ; x ; y m). Pressing on a window's title and dragging moves the window with the pointer.
 */
class DraggingAWindowTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(14, 6);
  private final Desktop desktop = toolkit.desktop();

  @Test
  void draggingAWindowByItsTitleMovesItWithThePointer() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    press(mouse(0, 3, 0, 'M') + mouse(32, 4, 1, 'M') + mouse(32, 6, 2, 'M') + mouse(0, 6, 2, 'm'));
    desktop.draw(screen);

    assertEquals(List.of(
        "              ",
        "              ",
        "   ╔═ A ══╗   ",
        "   ║      ║   ",
        "   ╚══════╝   "), screen.lines().subList(0, 5));
  }

  @Test
  void movingThePointerAfterTheReleaseMovesNothingAndADragFromInsideAWindowMovesNothing() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    press(mouse(0, 3, 0, 'M') + mouse(0, 3, 0, 'm') + mouse(32, 6, 2, 'M')
        + mouse(0, 3, 1, 'M') + mouse(32, 6, 3, 'M') + mouse(0, 6, 3, 'm'));
    desktop.draw(screen);

    assertEquals("╔═ A ══╗      ", screen.lines().getFirst());
  }

  @Test
  void theKeyboardHandsPressesDragsAndReleasesOfTheLeftButtonToTheDesktop() {
    Desktop recorded = org.mockito.Mockito.mock(Desktop.class);

    toolkit.keyboard(new ByteArrayInputStream((mouse(0, 1, 1, 'M') + mouse(32, 2, 3, 'M') + mouse(0, 2, 3, 'm'))
        .getBytes(StandardCharsets.UTF_8))).typeInto(recorded);

    org.mockito.InOrder order = org.mockito.Mockito.inOrder(recorded);
    order.verify(recorded).click(toolkit.point(1, 1));
    order.verify(recorded).drag(toolkit.point(2, 3));
    order.verify(recorded).release(toolkit.point(2, 3));
  }

  @Test
  void pressingOnTheTitleOfAWindowBelowRaisesItAndTheSameDragMovesIt() {
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");
    below.place(toolkit.area(0, 0, 6, 3));
    above.place(toolkit.area(7, 0, 6, 3));
    desktop.open(below);
    desktop.open(above);
    desktop.draw(screen);

    press(mouse(0, 2, 0, 'M') + mouse(32, 4, 2, 'M') + mouse(0, 4, 2, 'm'));
    desktop.draw(screen);

    assertEquals("╔ B ═╗", screen.lines().get(2).substring(2, 8));
  }

  @Test
  /** The press is in the window's width, on its title row that the bar covers, and away from the bar's titles. */
  void aDragThatStartsOnTheMenuBarDoesNotMoveTheWindowUnderIt() {
    MenuBar bar = toolkit.menuBar();
    bar.add(toolkit.menu("Datei"));
    desktop.show(bar);
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 14, 3));
    desktop.open(window);
    desktop.draw(screen);

    press(mouse(0, 10, 0, 'M') + mouse(32, 12, 2, 'M') + mouse(0, 12, 2, 'm'));
    desktop.draw(screen);

    assertEquals("║            ║", screen.lines().get(1));
  }

  @Test
  void draggingTheBottomRightCornerResizesTheWindowAndTheTopLeftStays() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    press(mouse(0, 7, 2, 'M') + mouse(32, 9, 3, 'M') + mouse(32, 10, 4, 'M') + mouse(0, 10, 4, 'm'));
    desktop.draw(screen);

    assertEquals(List.of(
        "╔═══ A ═══╗   ",
        "║         ║   ",
        "║         ║   ",
        "║         ║   ",
        "╚═════════╝   "), screen.lines().subList(0, 5));
  }

  @Test
  void aWindowShrinksNoSmallerThanItsFrameWithARoomInside() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(2, 1, 8, 4));
    desktop.open(window);
    desktop.draw(screen);

    press(mouse(0, 9, 4, 'M') + mouse(32, 0, 0, 'M') + mouse(0, 0, 0, 'm'));
    desktop.draw(screen);

    assertEquals(List.of(
        "  ╔═╗         ",
        "  ║ ║         ",
        "  ╚═╝         "), screen.lines().subList(1, 4));
  }

  /** A mouse report for a cell counted from 0: the button code, then M when pressed or moved and m when released. */
  private static String mouse(int button, int column, int row, char kind) {
    return "\u001b[<" + button + ";" + (column + 1) + ";" + (row + 1) + kind;
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
