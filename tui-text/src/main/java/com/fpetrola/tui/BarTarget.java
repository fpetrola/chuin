package com.fpetrola.tui;

/** Where a scroll bar was drawn, from its first cell on, down or across, so a click on one of its cells goes to it. */
class BarTarget implements ClickTarget {
  private final ScrollBar bar;
  private final Point from;
  private final Point step;
  private final int length;

  BarTarget(ScrollBar bar, Point from, Point step, int length) {
    this.bar = bar;
    this.from = from;
    this.step = step;
    this.length = length;
  }

  /** A click on one of its cells goes to the bar, which may do nothing with it, as one that is not needed; else otherwise. */
  @Override
  public void click(Point cell, Runnable otherwise) {
    int at = step.column() * (cell.column() - from.column()) + step.row() * (cell.row() - from.row());
    boolean inLine = step.column() == 0 ? cell.column() == from.column() : cell.row() == from.row();
    Settle pending = new PendingAction(otherwise);
    if (inLine && at >= 0 && at < length) {
      bar.accept(new BarClick(at, pending));
    }
    pending.settle();
  }
}
