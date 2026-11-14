package com.fpetrola.tui;

import java.util.List;

/** Draws the views it visits on a screen; only the focused button is drawn between brackets. */
class Painter implements ViewVisitor {
  private final Screen screen;
  private final int focused;
  private int seen;
  private int insideColumn;
  private int insideRow;
  private int column;
  private int row;

  Painter(Screen screen, int focused) {
    this.screen = screen;
    this.focused = focused;
  }

  @Override
  public void window(String title, Area area) {
    int inner = area.columns() - 2;
    String label = " " + title + " ";
    int left = (inner - label.length()) / 2;
    int right = inner - label.length() - left;
    screen.write(area.column(), area.row(), "┌" + "─".repeat(left) + label + "─".repeat(right) + "┐");
    String middle = "│" + " ".repeat(inner) + "│";
    for (int line = 1; line < area.rows() - 1; line++) {
      screen.write(area.column(), area.row() + line, middle);
    }
    screen.write(area.column(), area.row() + area.rows() - 1, "└" + "─".repeat(inner) + "┘");
    insideColumn = area.column() + 1;
    insideRow = area.row() + 1;
    column = insideColumn;
    row = insideRow;
  }

  @Override
  public void child(Point position) {
    column = insideColumn + position.column();
    row = insideRow + position.row();
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    String look = seen == focused ? "[ " + caption + " ]" : "  " + caption + "  ";
    screen.write(column, row, look);
    seen++;
  }

  @Override
  public void label(String text) {
    screen.write(column, row, text);
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    mark.accept(new MarkVisitor() {
      @Override
      public void checked() {
        screen.write(column, row, "[X] " + text);
      }

      @Override
      public void unchecked() {
        screen.write(column, row, "[ ] " + text);
      }
    });
    seen++;
  }
}
