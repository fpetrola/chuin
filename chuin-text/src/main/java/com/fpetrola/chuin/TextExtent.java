package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;

class TextExtent implements Extent {
  private final Area area;

  TextExtent(Area area) {
    this.area = area;
  }

  @Override
  public boolean covers(Point cell) {
    boolean across = cell.column() >= area.column() && cell.column() < area.column() + area.columns();
    boolean down = cell.row() >= area.row() && cell.row() < area.row() + area.rows();
    return across && down;
  }

  @Override
  public Area within(Area shown) {
    int left = Math.max(area.column(), shown.column());
    int top = Math.max(area.row(), shown.row());
    int right = Math.min(area.column() + area.columns(), shown.column() + shown.columns());
    int bottom = Math.min(area.row() + area.rows(), shown.row() + shown.rows());
    return new TextArea(left, top, Math.max(0, right - left), Math.max(0, bottom - top));
  }

  @Override
  public Point from(Point cell) {
    return new TextPoint(cell.column() - area.column(), cell.row() - area.row());
  }
}
