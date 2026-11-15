package com.fpetrola.chuin;

/** Counts focusable views from the first, the focused one at a place in that count. */
class TextFocusOrder implements FocusOrder {
  private final int focused;
  private int seen;

  TextFocusOrder(int focused) {
    this.focused = focused;
  }

  @Override
  public boolean next() {
    return seen++ == focused;
  }
}
