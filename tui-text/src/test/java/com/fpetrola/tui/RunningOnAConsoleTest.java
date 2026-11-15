package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RunningOnAConsoleTest {
  private static final String ESC = "\u001b[";
  /** Asks the terminal to report mouse presses and releases, in the SGR form. */
  private static final String MOUSE_ON = ESC + "?1000h" + ESC + "?1002h" + ESC + "?1006h";
  /** Turns the mouse off and shows the cursor again, as the console was before. */
  private static final String MOUSE_OFF = ESC + "?1006l" + ESC + "?1002l" + ESC + "?1000l" + ESC + "?25h";
  /** Black on the grey of the DOS palette, as exact RGB. */
  private static final String WINDOW = ESC + "0;38;2;0;0;0;48;2;168;168;168m";
  private static final String FRAME = ESC + "2J"
      + ESC + "1;1H" + WINDOW + "╔══ A ═══╗"
      + ESC + "2;1H" + WINDOW + "║        ║"
      + ESC + "3;1H" + WINDOW + "╚════════╝"
      + ESC + "0m" + ESC + "?25l";

  @Test
  void theDesktopIsShownOnceAtStartAndAgainAfterEveryKey() {
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
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
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
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

  @Test
  void theDesktopIsShownAgainAfterEveryPressDragAndReleaseOfTheMouse() {
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
    String pressDragRelease = "\u001b[<0;2;1M" + "\u001b[<32;4;2M" + "\u001b[<0;4;2m";
    ByteArrayInputStream mouse = new ByteArrayInputStream(pressDragRelease.getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    Console console = toolkit.console(mouse, shown, toolkit.screen(10, 3));
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 6, 2));
    desktop.open(window);
    console.run(desktop);

    assertEquals(4, shown.toString(StandardCharsets.UTF_8).split(java.util.regex.Pattern.quote(ESC + "2J"), -1).length - 1);
  }
}
