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
  /** The ANSI color each tint of the palette is shown in, the way Turbo Vision colors its desktop. */
  private static final Map<Character, String> COLORS = Map.of(
      '.', "0;37;44",
      'w', "0;30;47",
      'f', "0;97;42",
      'b', "0;30;42",
      'i', "0;97;44",
      'h', "0;31;47",
      'k', "0;93;42",
      'c', "0;30;46",
      's', "0;90;40");
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
}
