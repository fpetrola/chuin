package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.screen.Screen;


/** The shadow an area casts, one cell to its right and one cell below it: what is under it keeps its characters, greyed. */
class Shadow implements Drawing {
  private final Area area;

  Shadow(Area area) {
    this.area = area;
  }

  @Override
  public void drawOn(Screen screen) {
    int right = area.column() + area.columns();
    int below = area.row() + area.rows();
    for (int line = area.row() + 1; line < below; line++) {
      TintRole.SHADOW.paint(screen, right, line, 1);
    }
    TintRole.SHADOW.paint(screen, area.column() + 1, below, area.columns());
  }
}
