package com.fpetrola.tui;

/** A box drawn around an area: its border, a title centred on the top border when it has one, and a blank inside, all in one tint. */
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
    String label = title.isEmpty() ? "" : " " + title + " ";
    int left = (inner - label.length()) / 2;
    int right = inner - label.length() - left;
    int last = area.rows() - 1;
    String across = String.valueOf(border.across);
    String down = String.valueOf(border.down);
    screen.write(area.column(), area.row(), border.topLeft + across.repeat(left) + label + across.repeat(right) + border.topRight);
    for (int line = 1; line < last; line++) {
      screen.write(area.column(), area.row() + line, down + " ".repeat(inner) + down);
    }
    screen.write(area.column(), area.row() + last, border.bottomLeft + across.repeat(inner) + border.bottomRight);
    for (int line = 0; line <= last; line++) {
      screen.paint(area.column(), area.row() + line, area.columns(), tint);
    }
  }
}
