package com.fpetrola.chuin.text.menu;

import com.fpetrola.chuin.text.view.Caption;

class TextEntryPlacer implements EntryPlacer {
  /** Where the next entry starts. */
  private int column;

  @Override
  public PlacedEntry place(Caption caption) {
    PlacedEntry placed = new TextPlacedEntry(column, caption);
    column += caption.entryWidth();
    return placed;
  }
}
