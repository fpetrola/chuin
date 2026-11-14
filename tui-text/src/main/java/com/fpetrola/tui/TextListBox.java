package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;

/** Keeps a selection that stays between the ends, and the first row shown, which follows the selection. */
class TextListBox implements ListBox {
  /** ▲, at least one cell of track, and ▼. */
  private static final int FEWEST_ROWS = 3;
  private final List<String> items;
  private final int rows;
  private int top;
  private int selected;

  TextListBox(List<String> items, int rows) {
    if (rows < FEWEST_ROWS) {
      throw new IllegalArgumentException("a list box needs " + FEWEST_ROWS + " rows for its scroll bar, not " + rows);
    }
    this.items = List.copyOf(items);
    this.rows = rows;
  }

  @Override
  public Optional<String> selected() {
    return items.isEmpty() ? Optional.empty() : Optional.of(items.get(selected));
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.listBox(this, items, rows, top, selected, this::previous, this::next);
  }

  private void previous() {
    selected = Math.max(0, selected - 1);
    top = Math.min(top, selected);
  }

  private void next() {
    selected = Math.max(0, Math.min(items.size() - 1, selected + 1));
    top = Math.max(top, selected - rows + 1);
  }
}
