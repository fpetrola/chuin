package com.fpetrola.chuin.text.paint;

/** Counts focusable views from the first, the focused one at a place in that count. */
public class TextFocusOrder implements FocusOrder {
  private final int focused;
  private int seen;

  public TextFocusOrder(int focused) {
    this.focused = focused;
  }

  @Override
  public boolean next() {
    return seen++ == focused;
  }
}
