package com.fpetrola.chuin;

import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.DropDownItem;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.List;
import java.util.Optional;

/** Keeps the chosen item and, while the list is open, the highlighted one: no highlight means the list is closed. */
class TextDropDown implements DropDown {
  /** The ↓ after the field. */
  private static final int ARROW = 1;
  private final List<String> items;
  private int chosen;
  private Optional<Integer> highlighted = Optional.empty();

  TextDropDown(List<String> items) {
    this.items = List.copyOf(items);
  }

  @Override
  public Optional<String> chosen() {
    return items.isEmpty() ? Optional.empty() : Optional.of(items.get(chosen));
  }

  @Override
  public int width() {
    return items.stream().mapToInt(String::length).max().orElse(0);
  }

  @Override
  public void down() {
    highlighted = Optional.of(highlighted.map(at -> Math.min(at + 1, items.size() - 1)).orElse(chosen));
  }

  @Override
  public void up() {
    highlighted = highlighted.map(at -> Math.max(0, at - 1));
  }

  @Override
  public void choose() {
    highlighted.ifPresent(at -> chosen = at);
    close();
  }

  @Override
  public void close() {
    highlighted = Optional.empty();
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitDropDown(this);
    highlighted.ifPresent(at -> visitItems(visitor, at));
  }

  private void visitItems(ViewVisitor visitor, int highlight) {
    for (int at = 0; at < items.size(); at++) {
      DropDownItem item = new Item(at);
      if (at == highlight) {
        visitor.visitHighlightedDropDownItem(item);
      } else {
        visitor.visitDropDownItem(item);
      }
    }
  }

  /** An item of the open list: its place among the items, which gives its text and what choosing it does. */
  private class Item implements DropDownItem {
    private final int at;

    Item(int at) {
      this.at = at;
    }

    @Override
    public DropDown dropDown() {
      return TextDropDown.this;
    }

    @Override
    public String text() {
      return items.get(at);
    }

    @Override
    public void choose() {
      highlighted = Optional.of(at);
      TextDropDown.this.choose();
    }
  }

  @Override
  public Size size() {
    return new TextSize(width() + ARROW, 1);
  }
}
