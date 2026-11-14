package com.fpetrola.tui;

import java.util.List;

/**
 * Draws rows stacked one under the other, the way a list box walks its rows and a group of radio buttons its
 * options: a list row is its item padded to the longest, a blank and its scroll bar cell; an option is its mark and
 * its text. The selected row is tinted when the view has the focus.
 */
class RowPainter implements ViewVisitor {
  private static final char FOCUS_TINT = 'f';
  /** The blank and the scroll bar cell after a list item. */
  private static final int BESIDE_ITEM = 2;
  private final Screen screen;
  private final Area at;
  private final boolean focused;
  /** Where focusable views were drawn; this view's place in it grows a row with every row drawn. */
  private final List<Area> drawn;
  private final int place;
  private int shown;

  RowPainter(Screen screen, Area at, boolean focused, List<Area> drawn) {
    this.screen = screen;
    this.at = at;
    this.focused = focused;
    this.drawn = drawn;
    this.place = drawn.size();
    drawn.add(new TextArea(at.column(), at.row(), at.columns(), 0));
  }

  @Override
  public void visitListItem(ListItem item) {
    itemRow(item.text());
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    itemRow(item.text());
    tint(at.columns() - BESIDE_ITEM);
  }

  @Override
  public void visitNoListItem(ListBox box) {
    itemRow("");
  }

  private void itemRow(String text) {
    int width = at.columns() - BESIDE_ITEM;
    screen.write(at.column(), at.row() + shown, text + " ".repeat(width - text.length() + 1));
  }

  @Override
  public void visitScrollUp(ListBox box) {
    bar("▲");
  }

  @Override
  public void visitScrollTrack(ListBox box) {
    bar("░");
  }

  @Override
  public void visitScrollThumb(ListBox box) {
    bar("■");
  }

  @Override
  public void visitScrollDown(ListBox box) {
    bar("▼");
  }

  @Override
  public void visitNoScrollBar(ListBox box) {
    bar(" ");
  }

  @Override
  public void visitOption(Option option) {
    nextRow("( ) " + option.text());
  }

  @Override
  public void visitChosenOption(Option option) {
    tint(at.columns());
    nextRow("(•) " + option.text());
  }

  private void bar(String cell) {
    screen.write(at.column() + at.columns() - 1, at.row() + shown, cell);
    shown++;
    drawn.set(place, new TextArea(at.column(), at.row(), at.columns(), shown));
  }

  private void nextRow(String line) {
    screen.write(at.column(), at.row() + shown, line);
    shown++;
    drawn.set(place, new TextArea(at.column(), at.row(), at.columns(), shown));
  }

  private void tint(int cells) {
    if (focused) {
      screen.paint(at.column(), at.row() + shown, cells, FOCUS_TINT);
    }
  }
}
