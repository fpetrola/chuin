package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
  public int width() {
    return items.stream().mapToInt(String::length).max().orElse(0);
  }

  @Override
  public void acceptRows(ListRowVisitor visitor) {
    for (int shown = 0; shown < rows; shown++) {
      item(top + shown, visitor);
      bar(shown, visitor);
    }
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitListBox(this);
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

  private void item(int at, ListRowVisitor visitor) {
    String text = at < items.size() ? items.get(at) : "";
    Consumer<ListRowVisitor> present = at == selected ? row -> row.selectedItem(text) : row -> row.item(text);
    Consumer<ListRowVisitor> shown = at < items.size() ? present : ListRowVisitor::noItem;
    shown.accept(visitor);
  }

  /** ▲ on the first row, ▼ on the last, the thumb where the selection sits on the track between; nothing when every item fits. */
  private void bar(int shown, ListRowVisitor visitor) {
    int thumb = 1 + selected * (rows - 3) / Math.max(1, items.size() - 1);
    Consumer<ListRowVisitor> track = shown == thumb ? ListRowVisitor::barThumb : ListRowVisitor::barTrack;
    Consumer<ListRowVisitor> down = shown == rows - 1 ? ListRowVisitor::barDown : track;
    Consumer<ListRowVisitor> up = shown == 0 ? ListRowVisitor::barUp : down;
    Consumer<ListRowVisitor> cell = items.size() > rows ? up : ListRowVisitor::noBar;
    cell.accept(visitor);
  }
}
