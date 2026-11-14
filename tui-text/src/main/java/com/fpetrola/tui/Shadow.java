package com.fpetrola.tui;

/** The shadow an area casts: one cell to its right and one cell below it. */
class Shadow implements Drawing {
  private static final String CELL = "░";
  private static final char TINT = 's';
  private final Area area;

  Shadow(Area area) {
    this.area = area;
  }

  @Override
  public void drawOn(Screen screen) {
    int right = area.column() + area.columns();
    int below = area.row() + area.rows();
    for (int line = area.row() + 1; line < below; line++) {
      screen.write(right, line, CELL);
      screen.paint(right, line, 1, TINT);
    }
    screen.write(area.column() + 1, below, CELL.repeat(area.columns()));
    screen.paint(area.column() + 1, below, area.columns(), TINT);
  }
}
