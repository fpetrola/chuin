package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.screen.Screen;

import java.util.List;
import java.util.Optional;

/** A screen seen through an area of another: whatever is written or painted outside the area is cut off. */
class ClippedScreen implements Screen {
  private final Screen screen;
  private final Area area;

  ClippedScreen(Screen screen, Area area) {
    this.screen = screen;
    this.area = area;
  }

  @Override
  public List<String> lines() {
    return screen.lines();
  }

  @Override
  public void write(int column, int row, String text) {
    int from = Math.max(0, area.column() - column);
    int to = Math.min(text.length(), area.column() + area.columns() - column);
    if (inside(row) && from < to) {
      screen.write(column + from, row, text.substring(from, to));
    }
  }

  @Override
  public List<String> tints() {
    return screen.tints();
  }

  @Override
  public void paint(int column, int row, int columns, char tint) {
    int from = Math.max(column, area.column());
    int to = Math.min(column + columns, area.column() + area.columns());
    if (inside(row) && from < to) {
      screen.paint(from, row, to - from, tint);
    }
  }

  private boolean inside(int row) {
    return row >= area.row() && row < area.row() + area.rows();
  }

  @Override
  public void showCursorAt(Point cell) {
    boolean across = cell.column() >= area.column() && cell.column() < area.column() + area.columns();
    if (across && inside(cell.row())) {
      screen.showCursorAt(cell);
    }
  }

  @Override
  public void hideCursor() {
    screen.hideCursor();
  }

  @Override
  public Optional<Point> cursor() {
    return screen.cursor();
  }
}
