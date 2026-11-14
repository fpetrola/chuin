package com.fpetrola.tui;

import java.util.List;

/** Keeps which one of its options is chosen, between the first and the last. */
class TextRadioButtons implements RadioButtons {
  /** The cells "(•) " takes before each option. */
  private static final int MARK = 4;
  private final List<String> options;
  private int chosen;

  TextRadioButtons(List<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("a group of radio buttons needs at least one option");
    }
    this.options = List.copyOf(options);
  }

  @Override
  public int width() {
    return MARK + options.stream().mapToInt(option -> new TextCaption(option).shown().length()).max().orElse(0);
  }

  @Override
  public void choose(int option) {
    if (option >= 0 && option < options.size()) {
      chosen = option;
    }
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitRadioButtons(this);
    for (int at = 0; at < options.size(); at++) {
      Option option = new TextOption(this, options.get(at));
      if (at == chosen) {
        visitor.visitChosenOption(option);
      } else {
        visitor.visitOption(option);
      }
    }
  }

  @Override
  public void previous() {
    chosen = Math.max(0, chosen - 1);
  }

  @Override
  public void next() {
    chosen = Math.min(options.size() - 1, chosen + 1);
  }
}
