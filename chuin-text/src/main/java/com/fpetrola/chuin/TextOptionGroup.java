package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.List;

/** Keeps which one of its options is chosen, between the first and the last. */
class TextOptionGroup implements OptionGroup {
  /** The cells "(•) " takes before each option. */
  private static final int MARK = 4;
  private final List<String> options;
  private final Cursor chosen;

  TextOptionGroup(List<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("a group needs at least one option");
    }
    this.options = List.copyOf(options);
    this.chosen = new TextCursor(this.options.size());
  }

  @Override
  public int width() {
    return MARK + options.stream().mapToInt(option -> new TextCaption(option).shown().length()).max().orElse(0);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitOptionGroup(this);
    for (int at = 0; at < options.size(); at++) {
      Option option = new Place(at);
      if (at == chosen.at()) {
        visitor.visitChosenOption(option);
      } else {
        visitor.visitOption(option);
      }
    }
  }

  @Override
  public void previous() {
    chosen.step(-1);
  }

  @Override
  public void next() {
    chosen.step(1);
  }

  /** An option of the group: its place among the options, which gives its text and what choosing it does. */
  private class Place implements Option {
    private final int at;

    Place(int at) {
      this.at = at;
    }

    @Override
    public OptionGroup group() {
      return TextOptionGroup.this;
    }

    @Override
    public String text() {
      return options.get(at);
    }

    @Override
    public void choose() {
      chosen.moveTo(at);
    }
  }

  @Override
  public Size size() {
    return new TextSize(width(), options.size());
  }
}
