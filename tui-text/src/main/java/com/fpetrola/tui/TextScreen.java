package com.fpetrola.tui;

import java.util.Arrays;
import java.util.List;

/** A grid of cells; whatever is written outside it is cut off. */
class TextScreen implements Screen {
  private final char[][] cells;

  TextScreen(int columns, int rows) {
    cells = new char[rows][columns];
    Arrays.stream(cells).forEach(line -> Arrays.fill(line, ' '));
  }

  @Override
  public List<String> lines() {
    return Arrays.stream(cells).map(String::new).toList();
  }

  @Override
  public void write(int column, int row, String text) {
    if (row < 0 || row >= cells.length) {
      return;
    }
    char[] line = cells[row];
    int from = Math.max(0, -column);
    int to = Math.min(text.length(), line.length - column);
    for (int at = from; at < to; at++) {
      line[column + at] = text.charAt(at);
    }
  }
}
