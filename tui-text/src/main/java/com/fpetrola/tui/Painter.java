package com.fpetrola.tui;

import java.util.List;

/** Draws the views it visits on a screen; the focused button is drawn between brackets, the focused check box with a pointer before it. */
class Painter implements ViewVisitor {
  private static final char WINDOW_TINT = 'w';
  private static final char FOCUS_TINT = 'f';
  private final Screen screen;
  /** The inside of the window being drawn: its views cannot draw past it. */
  private Screen inside;
  /** The top left cell inside the frame of the window drawn last. */
  private int insideColumn;
  private int insideRow;
  /** Draws the rows of the list box or the options of the group visited last. */
  private ViewVisitor rows = new ViewVisitor() {
  };
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
    Area area = window.area();
    new RaisedFrame(area, window.title(), WINDOW_TINT).accept(screen);
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
    if (seen == focused) {
      inside.write(column - 1, row, ">");
    }
    next(("[ ] " + box.text()).length());
  }

  @Override
  public void visitChecked(CheckBox box) {
    inside.write(column, row, "[X] " + box.text());
  }

  @Override
  public void visitUnchecked(CheckBox box) {
    inside.write(column, row, "[ ] " + box.text());
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
    rows = new RowPainter(inside, new TextArea(column, row, box.width(), 1), seen++ == focused, drawn);
  }



  @Override
  public void visitListItem(ListItem item) {
    rows.visitListItem(item);
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    rows.visitSelectedListItem(item);
  }

  @Override
  public void visitNoListItem(ListBox box) {
    rows.visitNoListItem(box);
  }

  @Override
  public void visitScrollUp(ListBox box) {
    rows.visitScrollUp(box);
  }

  @Override
  public void visitScrollTrack(ListBox box) {
    rows.visitScrollTrack(box);
  }

  @Override
  public void visitScrollThumb(ListBox box) {
    rows.visitScrollThumb(box);
  }

  @Override
  public void visitScrollDown(ListBox box) {
    rows.visitScrollDown(box);
  }

  @Override
  public void visitNoScrollBar(ListBox box) {
    rows.visitNoScrollBar(box);
  }

  @Override
  public void visitOption(Option option) {
    rows.visitOption(option);
  }

  @Override
  public void visitChosenOption(Option option) {
    rows.visitChosenOption(option);
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    rows = new RowPainter(inside, new TextArea(column, row, group.width(), 1), seen++ == focused, drawn);
  }

}
