package com.fpetrola.tui;

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
  void aWindowScrollsFromItsOriginAndNeverBeforeTheTopLeft() {
    window.scroll(2, 3);
    window.scroll(-5, -1);

    assertEquals(toolkit.point(0, 2), window.origin());
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
