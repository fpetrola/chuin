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
        "║l 0     ║",
        "║l 1     ║",
        "║l 2     ║",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusOnAViewOutOfSightScrollsTheWindowJustEnoughToShowIt() {
    type("\tok");
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ W ═══╗",
        "║l 4     ║",
        "║l 5     ║",
        "║ok      ║",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusGoingBackToAViewAboveScrollsTheWindowBackUp() {
    type("\t");
    desktop.draw(screen);
    type("\t");
    desktop.draw(screen);

    assertEquals("║l 0     ║", screen.lines().get(1));
  }

  @Test
  void aWindowScrollsFromItsOriginAndNeverBeforeTheTopLeft() {
    window.scroll(2, 3);
    window.scroll(-5, -1);

    assertEquals(toolkit.point(0, 2), window.origin());
  }

  private void type(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
