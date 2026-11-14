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
  /** The inside of the window being drawn: its views cannot draw past it. */
  private Screen inside;
  /** The top left cell inside the frame of the window drawn last. */
  private int insideColumn;
  private int insideRow;
  private final int focused;
  /** Where each focusable view was drawn, in focus order, so a click can find what is under it. */
  private final List<Area> drawn;
  private int seen;
  private int column;
  private int row;

  Painter(Screen screen, int focused, List<Area> drawn) {
    this.screen = screen;
    this.inside = screen;
    this.focused = focused;
    this.drawn = drawn;
  }

  @Override
  public void visitWindow(Window window) {
    String title = window.title();
    Area area = window.area();
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
    inside = new ClippedScreen(screen, new TextArea(insideColumn, insideRow, area.columns() - 2, area.rows() - 2));
  }

  /** The view that comes next is drawn at this place inside the window drawn last. */
  @Override
  public void visitPlacement(Placement placement) {
    column = insideColumn + placement.position().column();
    row = insideRow + placement.position().row();
  }

  @Override
  public void visitButton(Button button) {
    String caption = button.caption();
    String look = seen == focused ? "[ " + caption + " ]" : "  " + caption + "  ";
    inside.write(column, row, look);
    next(look.length());
  }

  @Override
  public void visitLabel(Label label) {
    inside.write(column, row, label.text());
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    String text = box.text();
    if (seen == focused) {
      inside.write(column - 1, row, ">");
    }
    box.acceptMark(new MarkVisitor() {
      @Override
      public void checked() {
        inside.write(column, row, "[X] " + text);
      }

      @Override
      public void unchecked() {
        inside.write(column, row, "[ ] " + text);
      }
    });
    next(("[ ] " + text).length());
  }

  @Override
  public void visitInputLine(InputLine line) {
    String text = line.text();
    inside.write(column, row, text + "_".repeat(Math.max(0, line.width() - text.length())));
    next(Math.max(line.width(), text.length()));
  }

  /** Passes a focusable view of this many cells, tinting it when it is the focused one. */
  private void next(int cells) {
    drawn.add(new TextArea(column, row, cells, 1));
    if (seen++ == focused) {
      inside.paint(column, row, cells, FOCUS_TINT);
    }
  }

  @Override
  public void visitListBox(ListBox box) {
    box.acceptRows(new RowPainter(inside, new TextArea(column, row, box.width(), 1), seen++ == focused, drawn));
  }



  @Override
  public void visitRadioButtons(RadioButtons group) {
    group.acceptOptions(new RowPainter(inside, new TextArea(column, row, group.width(), 1), seen++ == focused, drawn));
  }

}
