package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.view.ScrollBar;



/** Where a scroll bar was drawn, from its first cell on, down or across, so a click on one of its cells goes to it. */
class BarTarget implements ClickTarget {
  private final ScrollBar bar;
  private final BarLine line;
  private final int length;
  /** Hands the drag that follows a press on the thumb to whoever follows the pointer. */
  private final Grab grab;

  BarTarget(ScrollBar bar, BarLine line, int length, Grab grab) {
    this.grab = grab;
    this.bar = bar;
    this.line = line;
    this.length = length;
  }

  /** A click on one of its cells goes to the bar, which may do nothing with it, as one that is not needed; else otherwise. */
  @Override
  public void click(Point cell, Runnable otherwise) {
    int at = line.along(cell);
    Settle pending = new PendingAction(otherwise);
    if (line.holds(cell) && at >= 0 && at < length) {
      bar.accept(new BarClick(at, pending, line, grab));
    }
    pending.settle();
  }
}
