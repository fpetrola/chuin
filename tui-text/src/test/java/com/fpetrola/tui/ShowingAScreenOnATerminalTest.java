package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ShowingAScreenOnATerminalTest {
  private static final String ESC = "\u001b[";

  @Test
  void aScreenIsShownLineByLineWithAnsiSequences() {
    Toolkit toolkit = new TextToolkit();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(out);
    Screen screen = toolkit.screen(6, 2);
    screen.write(1, 1, "┌hola");

    terminal.show(screen);

    assertEquals(
        ESC + "2J"
            + ESC + "1;1H" + "      "
            + ESC + "2;1H" + " ┌hola",
        out.toString(StandardCharsets.UTF_8));
  }
}
