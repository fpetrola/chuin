package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.screen.Screen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** A grid of cells; whatever is written outside it is cut off. */
class TextScreen implements Screen {
  private static final char UNTINTED = '.';
  private final char[][] cells;
  private final char[][] tints;
  /** Where typing goes, or nothing while the cursor is hidden. */
  private Optional<Point> cursor = Optional.empty();

  TextScreen(int columns, int rows) {
    cells = new char[rows][columns];
    Arrays.stream(cells).forEach(line -> Arrays.fill(line, ' '));
    tints = new char[rows][columns];
    Arrays.stream(tints).forEach(line -> Arrays.fill(line, UNTINTED));
  }

  @Override
  public List<String> lines() {
    return Arrays.stream(cells).map(String::new).toList();
  }

  @Override
  public List<String> tints() {
    return Arrays.stream(tints).map(String::new).toList();
  }

  @Override
  public void paint(int column, int row, int columns, char tint) {
    put(tints, column, row, String.valueOf(tint).repeat(Math.max(0, columns)));
  }

  @Override
  public void write(int column, int row, String text) {
    put(cells, column, row, text);
  }

  /** Copies text into a grid from a cell on, cutting whatever falls outside it. */
  private static void put(char[][] grid, int column, int row, String text) {
    if (row < 0 || row >= grid.length) {
      return;
    }
    char[] line = grid[row];
    int from = Math.max(0, -column);
    int to = Math.min(text.length(), line.length - column);
    for (int at = from; at < to; at++) {
      line[column + at] = text.charAt(at);
    }
  }

  @Override
  public void showCursorAt(Point cell) {
    cursor = Optional.of(cell);
  }

  @Override
  public void hideCursor() {
    cursor = Optional.empty();
  }

  @Override
  public Optional<Point> cursor() {
    return cursor;
  }
}
