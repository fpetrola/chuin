package com.fpetrola.tui;

import java.util.Arrays;
import java.util.List;

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
    System.arraycopy(text.toCharArray(), 0, cells[row], column, text.length());
  }
}
