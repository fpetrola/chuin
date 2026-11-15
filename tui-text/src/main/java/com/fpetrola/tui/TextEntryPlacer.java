package com.fpetrola.tui;

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
