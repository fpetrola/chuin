package com.fpetrola.tui;

/** Places a window a few cells away from where it says it is. */
class MoveWindow implements ViewVisitor {
  private final Window moved;
  private final int columns;
  private final int rows;

  MoveWindow(Window moved, int columns, int rows) {
    this.moved = moved;
    this.columns = columns;
    this.rows = rows;
  }

  @Override
  public void window(String title, Area area) {
    moved.place(new TextArea(area.column() + columns, area.row() + rows, area.columns(), area.rows()));
  }
}
