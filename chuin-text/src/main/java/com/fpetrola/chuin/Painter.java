package com.fpetrola.chuin;

import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.text.geometry.TextExtent;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.DropDownItem;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.ListItem;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.Placement;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ScrollPanel;
import com.fpetrola.chuin.view.ViewVisitor;
import com.fpetrola.chuin.view.Window;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/** Draws the views it visits on a screen; the focused button is drawn between brackets, the focused check box with a pointer before it. */
class Painter implements ViewVisitor {
  /** What a button's shadow may be drawn over: a blank cell or another button's shadow. */
  private static final String SHADE_COVERS = " ▀▄";
  private static final String BOX = "[ ] ";
  /** The lines the frame of its window is drawn with: double for the window on top, single for the rest. */
  private final Border border;
  /** What floats over every window, drawn after them all, such as a drop down's open list. */
  private final List<Drawing> popups;
  /** What a click on a window's frame may fall on: its scroll bars. */
  private final List<ClickTarget> targets;
  /** Hands a drag on to whoever follows the pointer, as a press on a bar's thumb does. */
  private final Grab grab;
  /** The list of the drop down visited last. */
  private ViewVisitor list = new ViewVisitor() {
  };
  private final Screen screen;
  /** The inside of the window being drawn: its views cannot draw past it. */
  private Screen inside;
  /** Where the place 0, 0 of the window drawn last falls on the screen: inside its frame, less how far it is scrolled. */
  /** How to go back to what is around each panel entered and not yet left, the innermost first. */
  private final Deque<Runnable> left = new ArrayDeque<>();
  /** How to cut what each panel entered and not yet left holds to what it shows, so a click finds only what shows. */
  private final Deque<Runnable> cuts = new ArrayDeque<>();
  private int insideColumn;
  private int insideRow;
  /** Draws the rows of the list box or the options of the group visited last. */
  private ViewVisitor rows = new ViewVisitor() {
  };
  /** Where each focusable view was drawn, in focus order, so a click can find what is under it. */
  private final List<Area> drawn;
  /** Which focusable view the walk is on, and whether it is the focused one. */
  private final FocusOrder order;
  private int column;
  private int row;

  Painter(Screen screen, int focused, List<Area> drawn, Border border, List<Drawing> popups, List<ClickTarget> targets,
      Grab grab) {
    this.grab = grab;
    this.popups = popups;
    this.targets = targets;
    this.border = border;
    this.screen = screen;
    this.inside = screen;
    this.order = new TextFocusOrder(focused);
    this.drawn = drawn;
  }

  @Override
  public void visitWindow(Window window) {
    Area area = window.area();
    new RaisedFrame(area, window.title(), TintRole.WINDOW, border).drawOn(screen);
    inside = screen;
    Area held = border.inside(area);
    column = held.column();
    row = held.row();
  }

  /** What a panel holds is drawn shifted by its origin and cut to the panel, less the last column and row its bars take. */
  @Override
  public void visitScrollPanel(ScrollPanel panel) {
    Screen outer = inside;
    int outerColumn = insideColumn;
    int outerRow = insideRow;
    int panelColumn = column;
    int panelRow = row;
    left.push(() -> {
      inside = outer;
      insideColumn = outerColumn;
      insideRow = outerRow;
      column = panelColumn;
      row = panelRow;
    });
    Size size = panel.size();
    Area shown = new TextArea(column, row, size.columns() - 1, size.rows() - 1);
    int first = drawn.size();
    cuts.push(() -> {
      for (int at = first; at < drawn.size(); at++) {
        drawn.set(at, new TextExtent(drawn.get(at)).within(shown));
      }
    });
    inside = new ClippedScreen(outer, shown);
    insideColumn = column - panel.origin().column();
    insideRow = row - panel.origin().row();
  }

  /** Back to what is around the panel, its bars drawn down its last column and across its last row, where a click finds them. */
  @Override
  public void leaveScrollPanel(ScrollPanel panel) {
    cuts.pop().run();
    left.pop().run();
    Size size = panel.size();
    Point down = new TextPoint(column + size.columns() - 1, row);
    Point across = new TextPoint(column, row + size.rows() - 1);
    BarLine downward = new TextBarLine(down, new TextPoint(0, 1));
    BarLine sideways = new TextBarLine(across, new TextPoint(1, 0));
    panel.vertical().accept(new BarCells(inside, downward, "▲", "▼"));
    panel.horizontal().accept(new BarCells(inside, sideways, "◄", "►"));
    targets.add(new BarTarget(panel.vertical(), downward, size.rows() - 1, grab));
    targets.add(new BarTarget(panel.horizontal(), sideways, size.columns() - 1, grab));
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
    TintRole.BUTTON.paint(inside, column, row, face.length());
    next(face.length(), () -> { });
    caption.highlight(inside, column + 2, row, TintRole.FOCUS_LETTER);
  }

  /** Moves the face just drawn a cell to the right and takes its shadow away, the button tint going with it. */
  @Override
  public void visitHeldButton(Button button) {
    Caption caption = new TextCaption(button.caption());
    String face = "  " + caption.shown() + "  ";
    char tint = inside.tints().get(row).charAt(column);
    unshade(column + face.length(), row);
    for (int cell = 1; cell <= face.length(); cell++) {
      unshade(column + cell, row + 1);
    }
    inside.write(column, row, " " + face);
    TintRole.WINDOW.paint(inside, column, row, 1);
    inside.paint(column + 1, row, face.length(), tint);
    caption.highlight(inside, column + 3, row, TintRole.FOCUS_LETTER);
  }

  @Override
  public void visitLabel(Label label) {
    Caption caption = new TextCaption(label.text());
    inside.write(column, row, caption.shown());
    caption.highlight(inside, column, row, TintRole.LETTER);
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    Caption caption = new TextCaption(box.text());
    int cells = BOX.length() + caption.shown().length();
    TintRole.GROUP.paint(inside, column, row, cells);
    caption.highlight(inside, column + BOX.length(), row, TintRole.GROUP_LETTER);
    next(cells, () -> {
      inside.write(column - 1, row, ">");
      caption.highlight(inside, column + BOX.length(), row, TintRole.FOCUS_LETTER);
    });
  }

  @Override
  public void visitChecked(CheckBox box) {
    inside.write(column, row, "[X] " + new TextCaption(box.text()).shown());
  }

  @Override
  public void visitUnchecked(CheckBox box) {
    inside.write(column, row, BOX + new TextCaption(box.text()).shown());
  }



  @Override
  public void visitInputLine(InputLine line) {
    String text = line.text();
    int cells = line.size().columns();
    inside.write(column, row, text + " ".repeat(cells - text.length()));
    TintRole.FIELD.paint(inside, column, row, cells);
    drawn.add(new TextArea(column, row, cells, 1));
    if (order.next()) {
      inside.showCursorAt(new TextPoint(column + Math.min(text.length(), cells - 1), row));
    }
  }

  /** Draws a cell of a button's shadow, but only on the window's own background, blank or shadowed already: it never covers what a view drew. */
  private void shade(int at, int line, char cell) {
    List<String> lines = inside.lines();
    boolean onScreen = line >= 0 && line < lines.size() && at >= 0 && at < lines.get(line).length();
    boolean free = onScreen && SHADE_COVERS.indexOf(lines.get(line).charAt(at)) >= 0;
    if (free && inside.tints().get(line).charAt(at) == TintRole.WINDOW.letter()) {
      inside.write(at, line, String.valueOf(cell));
    }
  }

  /** Takes away a button's shadow from a cell, if it is there. */
  private void unshade(int at, int line) {
    List<String> lines = inside.lines();
    boolean onScreen = line >= 0 && line < lines.size() && at >= 0 && at < lines.get(line).length();
    if (onScreen && "▄▀".indexOf(lines.get(line).charAt(at)) >= 0) {
      inside.write(at, line, " ");
    }
  }

  /** Passes a focusable view of this many cells; when it is the focused one, tints it and then does what the focus adds. */
  private void next(int cells, Runnable onFocus) {
    drawn.add(new TextArea(column, row, cells, 1));
    if (order.next()) {
      TintRole.FOCUS.paint(inside, column, row, cells);
      onFocus.run();
    }
  }

  @Override
  public void visitListBox(ListBox box) {
    rows = new RowPainter(inside, new TextArea(column, row, box.size().columns(), 1), order.next(), drawn);
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
  public void visitScrollUp(ScrollBar bar) {
    rows.visitScrollUp(bar);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    rows.visitScrollTrack(bar);
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    rows.visitScrollThumb(bar);
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    rows.visitScrollDown(bar);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    rows.visitNoScrollBar(bar);
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
  public void visitOptionGroup(OptionGroup group) {
    rows = new RowPainter(inside, new TextArea(column, row, group.size().columns(), 1), order.next(), drawn);
  }


  /** The field shows the chosen item and an arrow; the open list is left to be drawn last, over everything. */
  @Override
  public void visitDropDown(DropDown dropDown) {
    int width = dropDown.width();
    String chosen = dropDown.chosen().orElse("");
    inside.write(column, row, chosen + " ".repeat(width - chosen.length()) + "↓");
    TintRole.FIELD.paint(inside, column, row, dropDown.size().columns());
    drawn.add(new TextArea(column, row, dropDown.size().columns(), 1));
    // It takes its place in the focus order, but its field keeps its own tint when focused.
    order.next();
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
