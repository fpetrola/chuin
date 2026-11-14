package com.fpetrola.tui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

/** Shows screens on a console that understands ANSI escape sequences. */
class AnsiTerminal implements Terminal {
  private static final String ESC = "\u001b[";
  private final OutputStream out;

  AnsiTerminal(OutputStream out) {
    this.out = out;
  }

  @Override
  public void show(Screen screen) {
    List<String> lines = screen.lines();
    StringBuilder text = new StringBuilder(ESC + "2J");
    IntStream.range(0, lines.size())
        .forEach(row -> text.append(ESC).append(row + 1).append(";1H").append(lines.get(row)));
    try {
      out.write(text.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
