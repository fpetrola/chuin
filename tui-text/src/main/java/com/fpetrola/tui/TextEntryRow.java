package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;


class TextEntryRow implements EntryRow {
  private final Screen screen;
  private final int row;
  private final EntryPlacer placer = new TextEntryPlacer();

  TextEntryRow(Screen screen, int row) {
    this.screen = screen;
    this.row = row;
  }

  @Override
  public void put(Caption caption, Tint tint, Tint letter) {
    int column = placer.place(caption).start();
    String entry = " " + caption.shown() + " ";
    screen.write(column, row, entry);
    tint.paint(screen, column, row, caption.entryWidth());
    caption.highlight(screen, column + 1, row, letter);
  }
}
