package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClosingAWindowTest {
  /** What a terminal sends for Ctrl+W. */
  private static final String CONTROL_W = "\u0017";

  @Test
  void controlWClosesTheTopWindowAndTheOneBelowGetsBackItsFocus() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");

    below.place(toolkit.area(0, 0, 9, 4));
    below.add(toolkit.button("No"), toolkit.point(0, 0));
    below.add(toolkit.button("Ni"), toolkit.point(0, 1));
    above.place(toolkit.area(10, 0, 9, 4));
    above.add(toolkit.button("Si"), toolkit.point(0, 0));
    desktop.open(below);
    press(toolkit, desktop, "\t");
    desktop.open(above);
    press(toolkit, desktop, CONTROL_W);
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ B ══╗           ",
        "║  No  ▄║           ",
        "║  Ni  ▄║           ",
        "╚═══════╝           ",
        "                    "), screen.lines());
    assertEquals("ffffff", screen.tints().get(2).substring(1, 7));
  }

  @Test
  void closingTheLastWindowLeavesABareDesktopAndClosingAgainDoesNothing() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(6, 2);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 5, 2));
    desktop.open(window);
    press(toolkit, desktop, CONTROL_W + CONTROL_W + "\t\r ");
    desktop.draw(screen);

    assertEquals(List.of("      ", "      "), screen.lines());
    assertEquals(List.of("......", "......"), screen.tints());
  }

  private static void press(Toolkit toolkit, Desktop desktop, String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }

  @Test
  void closingAfterF6LeavesOnTopTheWindowThatWasBelowItWithItsOwnFocus() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(27, 5);
    Desktop desktop = toolkit.desktop();
    for (int at = 1; at <= 3; at++) {
      Window window = toolkit.window(String.valueOf(at));
      window.place(toolkit.area(9 * (at - 1), 0, 8, 4));
      window.add(toolkit.button("a" + at), toolkit.point(0, 0));
      window.add(toolkit.button("b" + at), toolkit.point(0, 1));
      desktop.open(window);
      press(toolkit, desktop, at == 2 ? "" : "\t");
    }
    String f6 = "\u001b[17~";

    press(toolkit, desktop, f6 + CONTROL_W);
    desktop.draw(screen);

    assertEquals("         │  a2  │ ║  a3  ║ ", screen.lines().get(1));
    assertEquals("         │  b2  │ ║  b3  ║ ", screen.lines().get(2));
    assertEquals("ffffff", screen.tints().get(2).substring(19, 25));
    assertEquals("bbbbbb", screen.tints().get(2).substring(10, 16));
  }
}
