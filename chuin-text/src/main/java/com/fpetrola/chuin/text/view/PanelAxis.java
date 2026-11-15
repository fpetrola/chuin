package com.fpetrola.chuin.text.view;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/** One way a panel scrolls, down or across: how far it is, as far as what it holds goes past what it shows. */
class PanelAxis implements Scrollable {
  private final IntSupplier scrolled;
  private final IntConsumer scroll;
  private final int shown;
  private final int held;

  PanelAxis(IntSupplier scrolled, IntConsumer scroll, int shown, int held) {
    this.scrolled = scrolled;
    this.scroll = scroll;
    this.shown = shown;
    this.held = held;
  }

  @Override
  public int position() {
    return scrolled.getAsInt();
  }

  @Override
  public int most() {
    return Math.max(0, held - shown);
  }

  @Override
  public boolean overflows() {
    return most() > 0;
  }

  @Override
  public void previous() {
    by(-1);
  }

  @Override
  public void next() {
    by(1);
  }

  @Override
  public void pageUp() {
    by(1 - shown);
  }

  @Override
  public void pageDown() {
    by(shown - 1);
  }

  @Override
  public void moveTo(int position) {
    by(position - position());
  }

  /** Scrolls by these many cells, staying between 0 and the most. */
  private void by(int cells) {
    scroll.accept(Math.max(0, Math.min(most(), position() + cells)) - position());
  }
}
