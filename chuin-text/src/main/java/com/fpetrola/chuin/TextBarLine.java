package com.fpetrola.chuin;

import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.geometry.Point;


/** A bar lying from its first cell on, each next cell a step further: (0, 1) down, (1, 0) across. */
class TextBarLine implements BarLine {
  private final Point from;
  private final Point step;

  TextBarLine(Point from, Point step) {
    this.from = from;
    this.step = step;
  }

  @Override
  public boolean holds(Point cell) {
    return step.column() == 0 ? cell.column() == from.column() : cell.row() == from.row();
  }

  @Override
  public int along(Point cell) {
    return step.column() * (cell.column() - from.column()) + step.row() * (cell.row() - from.row());
  }

  @Override
  public Point cell(int along) {
    return new TextPoint(from.column() + step.column() * along, from.row() + step.row() * along);
  }
}
