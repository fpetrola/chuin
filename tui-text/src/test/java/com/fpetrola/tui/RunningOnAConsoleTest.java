package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class RunningOnAConsoleTest {
  private static final String ESC = "\u001b[";
  private static final String FRAME = ESC + "2J"
      + ESC + "1;1H" + "┌── A ───┐"
      + ESC + "2;1H" + "│        │"
      + ESC + "3;1H" + "└────────┘";

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

    assertEquals(FRAME + FRAME, shown.toString(StandardCharsets.UTF_8));
  }
}
