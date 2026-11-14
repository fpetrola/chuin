package com.fpetrola.tui;

/** A box drawn around an area: its border, a title centred on the top border when it has one, and a blank inside, all in one tint. */
class Frame implements Drawing {
  private final Area area;
  private final String title;
  private final char tint;

  Frame(Area area, String title, char tint) {
    this.area = area;
    this.title = title;
    this.tint = tint;
  }

  @Override
  public void drawOn(Screen screen) {
    int inner = area.columns() - 2;
    String label = title.isEmpty() ? "" : " " + title + " ";
    int left = (inner - label.length()) / 2;
    int right = inner - label.length() - left;
    int last = area.rows() - 1;
    screen.write(area.column(), area.row(), "┌" + "─".repeat(left) + label + "─".repeat(right) + "┐");
    for (int line = 1; line < last; line++) {
      screen.write(area.column(), area.row() + line, "│" + " ".repeat(inner) + "│");
    }
    screen.write(area.column(), area.row() + last, "└" + "─".repeat(inner) + "┘");
    for (int line = 0; line <= last; line++) {
      screen.paint(area.column(), area.row() + line, area.columns(), tint);
    }
  }
}
