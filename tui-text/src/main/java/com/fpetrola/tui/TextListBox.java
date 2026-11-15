package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Size;
import com.fpetrola.tui.view.ListBox;
import com.fpetrola.tui.view.ListItem;
import com.fpetrola.tui.view.ScrollBar;
import com.fpetrola.tui.view.ViewVisitor;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/** Keeps a selection that stays between the ends, and the first row shown, which follows the selection. */
class TextListBox implements ListBox, Scrollable {
  /** ▲, at least one cell of track, and ▼. */
  private static final int FEWEST_ROWS = 3;
  /** The blank and the scroll bar cell after the longest item. */
  private static final int BESIDE_ITEM = 2;
  private final List<String> items;
  private final int rows;
  /** Beside the rows, its thumb where the selection is among the items. */
  private final ScrollBar bar;
  private int top;
  private int selected;

  TextListBox(List<String> items, int rows) {
    if (rows < FEWEST_ROWS) {
      throw new IllegalArgumentException("a list box needs " + FEWEST_ROWS + " rows for its scroll bar, not " + rows);
    }
    this.items = List.copyOf(items);
    this.rows = rows;
    this.bar = new TextScrollBar(this, rows);
  }

  @Override
  public Optional<String> selected() {
    return items.isEmpty() ? Optional.empty() : Optional.of(items.get(selected));
  }

  @Override
  public int width() {
    return items.stream().mapToInt(String::length).max().orElse(0) + BESIDE_ITEM;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitListBox(this);
    for (int shown = 0; shown < rows; shown++) {
      item(top + shown, visitor);
    }
    bar.accept(visitor);
  }

  @Override
  public void previous() {
    selected = Math.max(0, selected - 1);
    top = Math.min(top, selected);
  }

  @Override
  public void next() {
    selected = Math.max(0, Math.min(items.size() - 1, selected + 1));
    top = Math.max(top, selected - rows + 1);
  }

  @Override
  public void pageUp() {
    selected = Math.max(0, selected - (rows - 1));
    top = Math.min(top, selected);
  }

  @Override
  public void pageDown() {
    selected = Math.max(0, Math.min(items.size() - 1, selected + rows - 1));
    top = Math.max(top, selected - rows + 1);
  }

  private void item(int at, ViewVisitor visitor) {
    ListItem item = new Row(at);
    Consumer<ViewVisitor> present = at == selected ? row -> row.visitSelectedListItem(item) : row -> row.visitListItem(item);
    Consumer<ViewVisitor> shown = at < items.size() ? present : row -> row.visitNoListItem(this);
    shown.accept(visitor);
  }

  /** Selects the item at this position among them, and scrolls to keep it in sight. */
  @Override
  public void moveTo(int position) {
    selected = Math.max(0, Math.min(most(), position));
    top = Math.max(Math.min(top, selected), selected - rows + 1);
  }

  @Override
  public int position() {
    return selected;
  }

  @Override
  public int most() {
    return Math.max(0, items.size() - 1);
  }

  @Override
  public boolean overflows() {
    return items.size() > rows;
  }

  /** An item on a row: its place among the items, which gives its text and what selecting it does. */
  private class Row implements ListItem {
    private final int at;

    Row(int at) {
      this.at = at;
    }

    @Override
    public ListBox list() {
      return TextListBox.this;
    }

    @Override
    public String text() {
      return items.get(at);
    }

    @Override
    public void select() {
      selected = at;
    }
  }

  @Override
  public Size size() {
    return new TextSize(width(), rows);
  }
}
