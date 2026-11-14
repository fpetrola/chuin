package com.fpetrola.tui;

class TextEntryRow implements EntryRow {
  private final Screen screen;
  private final int row;
  private int column;

  TextEntryRow(Screen screen, int row) {
    this.screen = screen;
    this.row = row;
  }

  @Override
  public int next() {
    return column;
  }

  @Override
  public void put(Caption caption, char tint, char letter) {
    String entry = " " + caption.shown() + " ";
    screen.write(column, row, entry);
    screen.paint(column, row, entry.length(), tint);
    caption.highlight(screen, column + 1, row, letter);
    column += entry.length();
  }
}
