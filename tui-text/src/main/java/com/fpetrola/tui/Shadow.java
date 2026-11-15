package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.screen.Screen;


/** The shadow an area casts, one cell to its right and one cell below it: what is under it keeps its characters, greyed. */
class Shadow implements Drawing {
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
      screen.paint(right, line, 1, TINT);
    }
    screen.paint(area.column() + 1, below, area.columns(), TINT);
  }
}
