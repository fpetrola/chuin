package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.view.ScrollBar;



/** Where a scroll bar was drawn, from its first cell on, down or across, so a click on one of its cells goes to it. */
class BarTarget implements ClickTarget {
  private final ScrollBar bar;
  private final Point from;
  private final Point step;
  private final int length;
  /** Hands the drag that follows a press on the thumb to whoever follows the pointer. */
  private final Grab grab;

  BarTarget(ScrollBar bar, Point from, Point step, int length, Grab grab) {
    this.grab = grab;
    this.bar = bar;
    this.from = from;
    this.step = step;
    this.length = length;
  }

  /** A click on one of its cells goes to the bar, which may do nothing with it, as one that is not needed; else otherwise. */
  @Override
  public void click(Point cell, Runnable otherwise) {
    int at = along(cell);
    boolean inLine = step.column() == 0 ? cell.column() == from.column() : cell.row() == from.row();
    Settle pending = new PendingAction(otherwise);
    if (inLine && at >= 0 && at < length) {
      bar.accept(new BarClick(at, pending, () -> grab.hold(to -> bar.thumbTo(along(to)))));
    }
    pending.settle();
  }

  /** How far along the bar a cell is, counted from its first. */
  private int along(Point cell) {
    return step.column() * (cell.column() - from.column()) + step.row() * (cell.row() - from.row());
  }
}
