package com.fpetrola.tui;

/**
 * A click on a cell of a scroll bar, counted from its first: ▲ scrolls a step back, ▼ a step forward, the track a page
 * toward the click, and a press on the thumb takes it. What it does is noted while the bar walks its cells, and done after.
 */
class BarClick implements ViewVisitor {
  private final int at;
  private final Settle pending;
  /** What a press on the thumb does: takes it, for the drag that follows. */
  private final Runnable onThumb;
  private int walked;
  private boolean pastThumb;

  BarClick(int at, Settle pending, Runnable onThumb) {
    this.at = at;
    this.pending = pending;
    this.onThumb = onThumb;
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
    on(onThumb);
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    on(bar::next);
  }

  private void on(Runnable does) {
    if (walked++ == at) {
      pending.note(does);
    }
  }
}
