package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/** Keeps a selection that stays between the ends, and the first row shown, which follows the selection. */
class TextListBox implements ListBox {
  /** ▲, at least one cell of track, and ▼. */
  private static final int FEWEST_ROWS = 3;
  /** The blank and the scroll bar cell after the longest item. */
  private static final int BESIDE_ITEM = 2;
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
  public int width() {
    return items.stream().mapToInt(String::length).max().orElse(0) + BESIDE_ITEM;
  }

  @Override
  public void selectShown(int row) {
    int at = top + row;
    if (row >= 0 && row < rows && at < items.size()) {
      selected = at;
    }
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitListBox(this);
    for (int shown = 0; shown < rows; shown++) {
      item(top + shown, visitor);
      bar(shown, visitor);
    }
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
    ListItem item = new TextListItem(this, at < items.size() ? items.get(at) : "");
    Consumer<ViewVisitor> present = at == selected ? row -> row.visitSelectedListItem(item) : row -> row.visitListItem(item);
    Consumer<ViewVisitor> shown = at < items.size() ? present : row -> row.visitNoListItem(this);
    shown.accept(visitor);
  }

  /** ▲ on the first row, ▼ on the last, the thumb where the selection sits on the track between; nothing when every item fits. */
  private void bar(int shown, ViewVisitor visitor) {
    int thumb = 1 + selected * (rows - 3) / Math.max(1, items.size() - 1);
    Consumer<ViewVisitor> track = shown == thumb ? row -> row.visitScrollThumb(this) : row -> row.visitScrollTrack(this);
    Consumer<ViewVisitor> down = shown == rows - 1 ? row -> row.visitScrollDown(this) : track;
    Consumer<ViewVisitor> up = shown == 0 ? row -> row.visitScrollUp(this) : down;
    Consumer<ViewVisitor> cell = items.size() > rows ? up : row -> row.visitNoScrollBar(this);
    cell.accept(visitor);
  }
}
