package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class RunningOnAConsoleTest {
  private static final String ESC = "\u001b[";
  /** Asks the terminal to report mouse presses and releases, in the SGR form. */
  private static final String MOUSE_ON = ESC + "?1000h" + ESC + "?1006h";
  private static final String MOUSE_OFF = ESC + "?1006l" + ESC + "?1000l";
  private static final String WINDOW = ESC + "0;30;47m";
  private static final String FRAME = ESC + "2J"
      + ESC + "1;1H" + WINDOW + "╔══ A ═══╗"
      + ESC + "2;1H" + WINDOW + "║        ║"
      + ESC + "3;1H" + WINDOW + "╚════════╝"
      + ESC + "0m" + ESC + "?25l";

  @Test
  void theDesktopIsShownOnceAtStartAndAgainAfterEveryKey() {
    Toolkit toolkit = new TextToolkit();
    ByteArrayInputStream typed = new ByteArrayInputStream("x".getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    Console console = toolkit.console(typed, shown, toolkit.screen(10, 3));
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 10, 3));
    desktop.open(window);
    console.run(desktop);

    assertEquals(MOUSE_ON + FRAME + FRAME + MOUSE_OFF, shown.toString(StandardCharsets.UTF_8));
  }

  @Test
  void theDesktopIsShownAgainAfterAClick() {
    Toolkit toolkit = new TextToolkit();
    String leftPressedOnTheTopLeftCell = "\u001b[<0;1;1M";
    ByteArrayInputStream clicked = new ByteArrayInputStream(leftPressedOnTheTopLeftCell.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    Console console = toolkit.console(clicked, shown, toolkit.screen(10, 3));
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 10, 3));
    desktop.open(window);
    console.run(desktop);

    assertEquals(MOUSE_ON + FRAME + FRAME + MOUSE_OFF, shown.toString(StandardCharsets.UTF_8));
  }
}
