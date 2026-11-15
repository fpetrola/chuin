package com.fpetrola.tui;

import com.fpetrola.tui.view.ScrollBar;
import com.fpetrola.tui.view.ViewVisitor;


/**
 * A click on a cell of a scroll bar, counted from its first: ▲ scrolls a step back, ▼ a step forward, the track a page
 * toward the click, and a press on the thumb takes it, so the drag that follows moves it along the bar. What it does
 * is noted while the bar walks its cells, and done after.
 */
class BarClick implements ViewVisitor {
  private final int at;
  private final Settle pending;
  private final BarLine line;
  /** Follows the drag that comes after a press on the thumb, in the same cells as the line. */
  private final Grab grab;
  private int walked;
  private boolean pastThumb;

  BarClick(int at, Settle pending, BarLine line, Grab grab) {
    this.at = at;
    this.pending = pending;
    this.line = line;
    this.grab = grab;
  }

  @Override
  public void visitScrollUp(ScrollBar bar) {
    on(bar::previous);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    on(pastThumb ? bar::pageDown : bar::pageUp);
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    pastThumb = true;
    on(() -> grab.hold(to -> bar.thumbTo(line.along(to))));
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    on(bar::next);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    walked++;
  }

  private void on(Runnable does) {
    if (walked++ == at) {
      pending.note(does);
    }
  }
}
