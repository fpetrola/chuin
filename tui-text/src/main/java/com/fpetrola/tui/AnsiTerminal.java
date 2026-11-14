package com.fpetrola.tui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/** Shows screens on a console that understands ANSI escape sequences. */
class AnsiTerminal implements Terminal {
  private static final String ESC = "\u001b[";
  private static final String RESET = ESC + "0m";
  /** The tint of a cell no window or menu covers. */
  private static final char BARE = '.';
  /** What a blank cell of the bare desktop is shown as, the way Turbo Vision fills its background. */
  private static final char PATTERN = '░';
  /** The DOS palette Turbo Vision drew with: each colour as exact RGB, so no terminal theme can change it. */
  private static final String BLACK = "0;0;0";
  private static final String BLUE = "0;0;168";
  private static final String GREEN = "0;168;0";
  private static final String CYAN = "0;168;168";
  private static final String RED = "168;0;0";
  private static final String GREY = "168;168;168";
  private static final String DARK_GREY = "84;84;84";
  private static final String BRIGHT_YELLOW = "252;252;84";
  private static final String BRIGHT_WHITE = "252;252;252";
  /** The foreground and background each tint of the palette is shown in. */
  private static final Map<Character, String> COLORS = Map.ofEntries(
      Map.entry('.', colors(GREY, BLUE)),
      Map.entry('w', colors(BLACK, GREY)),
      Map.entry('m', colors(BLACK, GREY)),
      Map.entry('f', colors(BRIGHT_WHITE, GREEN)),
      Map.entry('b', colors(BLACK, GREEN)),
      Map.entry('i', colors(BRIGHT_WHITE, BLUE)),
      Map.entry('h', colors(RED, GREY)),
      Map.entry('k', colors(BRIGHT_YELLOW, GREEN)),
      Map.entry('c', colors(BLACK, CYAN)),
      Map.entry('q', colors(BRIGHT_YELLOW, CYAN)),
      Map.entry('s', colors(DARK_GREY, BLACK)));
  private final OutputStream out;

  AnsiTerminal(OutputStream out) {
    this.out = out;
  }

  @Override
  public void show(Screen screen) {
    List<String> lines = screen.lines();
    List<String> tints = screen.tints();
    StringBuilder text = new StringBuilder(ESC + "2J");
    IntStream.range(0, lines.size()).forEach(row -> {
      text.append(ESC).append(row + 1).append(";1H");
      colored(text, lines.get(row), tints.get(row));
    });
    text.append(RESET);
    text.append(screen.cursor().map(cell -> ESC + (cell.row() + 1) + ";" + (cell.column() + 1) + "H" + ESC + "?25h").orElse(ESC + "?25l"));
    try {
      out.write(text.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Appends a line, switching color only where its tint changes. */
  private void colored(StringBuilder text, String line, String tint) {
    for (int column = 0; column < line.length(); column++) {
      char current = tint.charAt(column);
      if (column == 0 || current != tint.charAt(column - 1)) {
        text.append(ESC).append(COLORS.get(current)).append('m');
      }
      char cell = line.charAt(column);
      text.append(cell == ' ' && current == BARE ? PATTERN : cell);
    }
  }

  /** The ANSI parameters that set a foreground and a background as exact RGB. */
  private static String colors(String foreground, String background) {
    return "0;38;2;" + foreground + ";48;2;" + background;
  }
}
