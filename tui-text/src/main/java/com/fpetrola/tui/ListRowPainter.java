package com.fpetrola.tui;

import java.util.List;

/** Draws the rows a list box walks, one under the other: the item padded to the longest, a blank, and the scroll bar cell. */
class ListRowPainter implements ListRowVisitor {
  private static final char FOCUS_TINT = 'f';
  private final Screen screen;
  private final int column;
  private final int row;
  private final int width;
  private final boolean focused;
  /** Where focusable views were drawn; this list box's place in it grows a row with every row drawn. */
  private final List<Area> drawn;
  private final int place;
  private int shown;

  ListRowPainter(Screen screen, Area at, boolean focused, List<Area> drawn) {
    this.screen = screen;
    this.column = at.column();
    this.row = at.row();
    this.width = at.columns();
    this.focused = focused;
    this.drawn = drawn;
    this.place = drawn.size();
    drawn.add(new TextArea(column, row, width + 2, 0));
  }

  @Override
  public void item(String text) {
    screen.write(column, row + shown, text + " ".repeat(width - text.length() + 1));
  }

  @Override
  public void selectedItem(String text) {
    item(text);
    if (focused) {
      screen.paint(column, row + shown, width, FOCUS_TINT);
    }
  }

  @Override
  public void noItem() {
    item("");
  }

  @Override
  public void barUp() {
    bar("▲");
  }

  @Override
  public void barTrack() {
    bar("░");
  }

  @Override
  public void barThumb() {
    bar("■");
  }

  @Override
  public void barDown() {
    bar("▼");
  }

  @Override
  public void noBar() {
    bar(" ");
  }

  private void bar(String cell) {
    screen.write(column + width + 1, row + shown, cell);
    shown++;
    drawn.set(place, new TextArea(column, row, width + 2, shown));
  }
}
