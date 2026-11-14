package com.fpetrola.tui;

import java.util.List;

/** Draws the views it visits on a screen; the focused button is drawn between brackets, the focused check box with a pointer before it. */
class Painter implements ViewVisitor {
  private static final char WINDOW_TINT = 'w';
  private static final char FOCUS_TINT = 'f';
  /** What a button's shadow may be drawn over: a blank cell or another button's shadow. */
  private static final String SHADE_COVERS = " ▀▄";
  /** The field of an input line, focused or not: white on blue. */
  private static final char FIELD_TINT = 'i';
  /** A highlighted letter on grey, and on the green face of a button. */
  private static final char LETTER = 'h';
  private static final char BUTTON_LETTER = 'k';
  /** The face of a button without the focus: black on green. */
  private static final char BUTTON_TINT = 'b';
  /** The lines the frame of its window is drawn with: double for the window on top, single for the rest. */
  private final Border border;
  /** What floats over every window, drawn after them all, such as a drop down's open list. */
  private final List<Drawing> popups;
  /** The list of the drop down visited last. */
  private ViewVisitor list = new ViewVisitor() {
  };
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

  Painter(Screen screen, int focused, List<Area> drawn, Border border, List<Drawing> popups) {
    this.popups = popups;
    this.border = border;
    this.screen = screen;
    this.inside = screen;
    this.focused = focused;
    this.drawn = drawn;
  }

  @Override
  public void visitWindow(Window window) {
    Area area = window.area();
    new RaisedFrame(area, window.title(), WINDOW_TINT, border).drawOn(screen);
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
    Caption caption = new TextCaption(button.caption());
    String face = "  " + caption.shown() + "  ";
    inside.write(column, row, face);
    shade(column + face.length(), row, '▄');
    for (int cell = 1; cell <= face.length(); cell++) {
      shade(column + cell, row + 1, '▀');
    }
    inside.paint(column, row, face.length(), BUTTON_TINT);
    next(face.length());
    caption.highlight(inside, column + 2, row, BUTTON_LETTER);
  }

  @Override
  public void visitLabel(Label label) {
    Caption caption = new TextCaption(label.text());
    inside.write(column, row, caption.shown());
    caption.highlight(inside, column, row, LETTER);
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
    int cells = Math.max(line.width(), text.length());
    inside.write(column, row, text + " ".repeat(cells - text.length()));
    inside.paint(column, row, cells, FIELD_TINT);
    drawn.add(new TextArea(column, row, cells, 1));
    if (seen++ == focused) {
      inside.showCursorAt(new TextPoint(column + Math.min(text.length(), cells - 1), row));
    }
  }

  /** Draws a cell of a button's shadow, but only on the window's own background, blank or shadowed already: it never covers what a view drew. */
  private void shade(int at, int line, char cell) {
    List<String> lines = inside.lines();
    boolean onScreen = line >= 0 && line < lines.size() && at >= 0 && at < lines.get(line).length();
    boolean free = onScreen && SHADE_COVERS.indexOf(lines.get(line).charAt(at)) >= 0;
    if (free && inside.tints().get(line).charAt(at) == WINDOW_TINT) {
      inside.write(at, line, String.valueOf(cell));
    }
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


  /** The field shows the chosen item and an arrow; the open list is left to be drawn last, over everything. */
  @Override
  public void visitDropDown(DropDown dropDown) {
    int width = dropDown.width();
    String chosen = dropDown.chosen().orElse("");
    inside.write(column, row, chosen + " ".repeat(width - chosen.length()) + "↓");
    inside.paint(column, row, width + 1, FIELD_TINT);
    drawn.add(new TextArea(column, row, width + 1, 1));
    seen++;
    DropDownList opened = new DropDownList(column, row + 1, width);
    list = opened;
    popups.add(opened);
  }

  @Override
  public void visitDropDownItem(DropDownItem item) {
    list.visitDropDownItem(item);
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    list.visitHighlightedDropDownItem(item);
  }
}
