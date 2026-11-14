package com.fpetrola.tui;

import java.util.List;

/** Keeps which one of its options is chosen, between the first and the last. */
class TextRadioButtons implements RadioButtons {
  private final List<String> options;
  private int chosen;

  TextRadioButtons(List<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("a group of radio buttons needs at least one option");
    }
    this.options = List.copyOf(options);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.radioButtons(this, options, chosen, this::previous, this::next);
  }

  private void previous() {
    chosen = Math.max(0, chosen - 1);
  }

  private void next() {
    chosen = Math.min(options.size() - 1, chosen + 1);
  }
}
