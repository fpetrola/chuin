package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.screen.Screen;


/** A box drawn around an area with a border: a title centred on the top row when it has one, and a blank inside, all in one tint. */
class Frame implements Drawing {
  private final Area area;
  private final String title;
  private final char tint;
  private final Border border;

  Frame(Area area, String title, char tint, Border border) {
    this.area = area;
    this.title = title;
    this.tint = tint;
    this.border = border;
  }

  @Override
  public void drawOn(Screen screen) {
    int inner = area.columns() - 2;
    int last = area.rows() - 1;
    screen.write(area.column(), area.row(), border.top(title.isEmpty() ? "" : " " + title + " ", inner));
    for (int line = 1; line < last; line++) {
      screen.write(area.column(), area.row() + line, border.middle(inner));
    }
    screen.write(area.column(), area.row() + last, border.bottom(inner));
    for (int line = 0; line <= last; line++) {
      screen.paint(area.column(), area.row() + line, area.columns(), tint);
    }
  }
}
