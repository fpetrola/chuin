package com.fpetrola.chuin;

import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.view.ViewVisitor;
import com.fpetrola.chuin.view.Window;


/** Places a window a few cells away from where it is. */
class MoveWindow implements ViewVisitor {
  private final int columns;
  private final int rows;

  MoveWindow(int columns, int rows) {
    this.columns = columns;
    this.rows = rows;
  }

  @Override
  public void visitWindow(Window window) {
    Area area = window.area();
    window.place(new TextArea(area.column() + columns, area.row() + rows, area.columns(), area.rows()));
  }
}
