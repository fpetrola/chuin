package com.fpetrola.tui;

import java.util.List;

/** Draws the views it visits on a screen; the focused button is drawn between brackets, the focused check box with a pointer before it. */
class Painter implements ViewVisitor {
  /** What a window casts one cell to its right and one cell below it. */
  private static final String SHADOW = "░";
  private static final char WINDOW_TINT = 'w';
  private static final char FOCUS_TINT = 'f';
  private static final char SHADOW_TINT = 's';
  private final Screen screen;
  private final int focused;
  /** Where each focusable view was drawn, in focus order, so a click can find what is under it. */
  private final List<Area> drawn;
  private int seen;
  private int insideColumn;
  private int insideRow;
  private int column;
  private int row;

  Painter(Screen screen, int focused, List<Area> drawn) {
    this.screen = screen;
    this.focused = focused;
    this.drawn = drawn;
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
    for (int line = 1; line < area.rows(); line++) {
      screen.write(area.column() + area.columns(), area.row() + line, SHADOW);
    }
    screen.write(area.column() + 1, area.row() + area.rows(), SHADOW.repeat(area.columns()));
    for (int line = 0; line < area.rows(); line++) {
      screen.paint(area.column(), area.row() + line, area.columns(), WINDOW_TINT);
      screen.paint(area.column() + area.columns(), area.row() + line + 1, 1, SHADOW_TINT);
    }
    screen.paint(area.column() + 1, area.row() + area.rows(), area.columns(), SHADOW_TINT);
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
    next(look.length());
  }

  @Override
  public void label(String text) {
    screen.write(column, row, text);
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    if (seen == focused) {
      screen.write(column - 1, row, ">");
    }
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
    next(("[ ] " + text).length());
  }

  @Override
  public void visitInputLine(InputLine line) {
    String text = line.text();
    screen.write(column, row, text + "_".repeat(Math.max(0, line.width() - text.length())));
    next(Math.max(line.width(), text.length()));
  }

  /** Passes a focusable view of this many cells, tinting it when it is the focused one. */
  private void next(int cells) {
    drawn.add(new TextArea(column, row, cells, 1));
    if (seen++ == focused) {
      screen.paint(column, row, cells, FOCUS_TINT);
    }
  }

  @Override
  public void visitListBox(ListBox box) {
    box.acceptRows(new RowPainter(screen, new TextArea(column, row, box.width() + 2, 1), seen++ == focused, drawn));
  }



  @Override
  public void visitRadioButtons(RadioButtons group) {
    group.acceptOptions(new RowPainter(screen, new TextArea(column, row, group.width(), 1), seen++ == focused, drawn));
  }

}
