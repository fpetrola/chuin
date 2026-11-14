package com.fpetrola.tui;

/** Draws the status line across the bottom row: each entry with a blank on each side, its marked key highlighted. */
class StatusPainter implements StatusVisitor {
  private static final char TINT = 'w';
  private static final char LETTER = 'h';
  private final Screen screen;
  /** The bottom row, which takes the entries left to right. */
  private final EntryRow entries;

  StatusPainter(Screen screen) {
    this.screen = screen;
    this.entries = new TextEntryRow(screen, screen.lines().size() - 1);
  }

  @Override
  public void visitStatusLine(StatusLine line) {
    int columns = screen.lines().getFirst().length();
    int row = screen.lines().size() - 1;
    screen.write(0, row, " ".repeat(columns));
    screen.paint(0, row, columns, TINT);
  }

  @Override
  public void visitStatusItem(StatusItem item) {
    entries.put(new TextCaption(item.text()), TINT, LETTER);
  }
}
