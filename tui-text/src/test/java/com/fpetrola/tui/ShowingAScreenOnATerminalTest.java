package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ShowingAScreenOnATerminalTest {
  private static final String ESC = "\u001b[";
  private static final String DESKTOP = ESC + "0;37;44m";
  private static final String WINDOW = ESC + "0;30;47m";
  private static final String FOCUS = ESC + "0;97;42m";
  private static final String SHADOW = ESC + "0;90;40m";

  @Test
  void aScreenIsShownLineByLineWithAnsiSequencesAndTheBareDesktopAsItsPattern() {
    Toolkit toolkit = new TextToolkit();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(out);
    Screen screen = toolkit.screen(6, 2);
    screen.write(1, 1, "┌hola");

    terminal.show(screen);

    assertEquals(
        ESC + "2J"
            + ESC + "1;1H" + DESKTOP + "░░░░░░"
            + ESC + "2;1H" + DESKTOP + "░┌hola"
            + ESC + "0m",
        out.toString(StandardCharsets.UTF_8));
  }

  @Test
  void eachRunOfCellsWithTheSameTintIsShownInItsColor() {
    Toolkit toolkit = new TextToolkit();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(out);
    Screen screen = toolkit.screen(7, 1);
    screen.write(0, 0, "abcdef");
    screen.paint(0, 0, 2, 'w');
    screen.paint(2, 0, 2, 'f');
    screen.paint(4, 0, 1, 's');

    terminal.show(screen);

    assertEquals(
        ESC + "2J"
            + ESC + "1;1H" + WINDOW + "ab" + FOCUS + "cd" + SHADOW + "e" + DESKTOP + "f░"
            + ESC + "0m",
        out.toString(StandardCharsets.UTF_8));
  }
}
