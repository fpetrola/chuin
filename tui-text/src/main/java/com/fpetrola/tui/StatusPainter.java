package com.fpetrola.tui;

/** Draws the status line across the bottom row: each entry with a blank on each side, its marked key highlighted. */
class StatusPainter implements StatusVisitor {
  private static final char TINT = 'w';
  private static final char LETTER = 'h';
  private final Screen screen;
  private final int row;
  /** What the entries drawn so far take across the row. */
  private final StringBuilder drawn = new StringBuilder();

  StatusPainter(Screen screen) {
    this.screen = screen;
    this.row = screen.lines().size() - 1;
  }

  @Override
  public void visitStatusLine(StatusLine line) {
    int columns = screen.lines().getFirst().length();
    screen.write(0, row, " ".repeat(columns));
    screen.paint(0, row, columns, TINT);
  }

  @Override
  public void visitStatusItem(StatusItem item) {
    Caption caption = new TextCaption(item.text());
    String entry = " " + caption.shown() + " ";
    screen.write(drawn.length(), row, entry);
    caption.highlight(screen, drawn.length() + 1, row, LETTER);
    drawn.append(entry);
  }
}
