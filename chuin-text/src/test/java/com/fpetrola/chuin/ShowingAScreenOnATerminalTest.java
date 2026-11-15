package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.screen.Terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ShowingAScreenOnATerminalTest {
  private static final String ESC = "\u001b[";
  /** The DOS palette Turbo Vision drew with, as exact RGB so no terminal theme can change it. */
  private static final String BLACK = "0;0;0";
  private static final String BLUE = "0;0;168";
  private static final String GREEN = "0;168;0";
  private static final String GREY = "168;168;168";
  private static final String DARK_GREY = "84;84;84";
  private static final String BRIGHT_WHITE = "252;252;252";
  private static final String DESKTOP = colors(GREY, BLUE);
  private static final String WINDOW = colors(BLACK, GREY);
  private static final String FOCUS = colors(BRIGHT_WHITE, GREEN);
  private static final String SHADOW = colors(DARK_GREY, BLACK);
  private static final String BUTTON = colors(BLACK, GREEN);

  private static String colors(String foreground, String background) {
    return ESC + "0;38;2;" + foreground + ";48;2;" + background + "m";
  }

  @Test
  void aScreenIsShownLineByLineWithAnsiSequencesAndTheBareDesktopAsItsPattern() {
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(out);
    Screen screen = toolkit.screen(6, 2);
    screen.write(1, 1, "┌hola");

    terminal.show(screen);

    assertEquals(
        ESC + "2J"
            + ESC + "1;1H" + DESKTOP + "░░░░░░"
            + ESC + "2;1H" + DESKTOP + "░┌hola"
            + ESC + "0m" + ESC + "?25l",
        out.toString(StandardCharsets.UTF_8));
  }

  @Test
  void eachRunOfCellsWithTheSameTintIsShownInItsColor() {
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
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
            + ESC + "0m" + ESC + "?25l",
        out.toString(StandardCharsets.UTF_8));
  }

  @Test
  void aButtonIsShownBlackOnTheGreenOfTheDosPaletteWhateverTheTerminalsTheme() {
    Toolkit toolkit = new TextToolkit(Map.of("COLORTERM", "truecolor"));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(out);
    Screen screen = toolkit.screen(2, 1);
    screen.write(0, 0, "Ok");
    screen.paint(0, 0, 2, 'b');

    terminal.show(screen);

    assertEquals(ESC + "2J" + ESC + "1;1H" + BUTTON + "Ok" + ESC + "0m" + ESC + "?25l", out.toString(StandardCharsets.UTF_8));
  }
}
