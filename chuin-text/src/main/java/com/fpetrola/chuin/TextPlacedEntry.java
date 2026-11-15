package com.fpetrola.chuin;

class TextPlacedEntry implements PlacedEntry {
  private final int start;
  private final int width;

  TextPlacedEntry(int start, Caption caption) {
    this.start = start;
    this.width = caption.entryWidth();
  }

  @Override
  public int start() {
    return start;
  }

  @Override
  public boolean covers(int column) {
    return column >= start && column < start + width;
  }
}
