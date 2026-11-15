package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.text.pointer.Grab;
import com.fpetrola.chuin.text.pointer.BarClick;
import com.fpetrola.chuin.text.pointer.BarLine;
import com.fpetrola.chuin.text.pointer.TextBarLine;
import com.fpetrola.chuin.text.pointer.Settle;
import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.ListItem;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;



/**
 * What a left click does to the view under it, once focused: holds a button down until the release, toggles a check box, chooses the option
 * on the row clicked, opens a drop down's list; an input line is only focused. On a list, the click selects the item on its row, or, on the
 * scroll bar, the item before (▲) or after (▼), or a page before or after (the track above or below the thumb). Every
 * action is only noted during the visit, and done once the walk is over.
 */
class ClickFocused implements ViewVisitor {
  /** What the click or key does once the walk is over. */
  private final Settle pending;
  /** Which column and row of the view was clicked, counted from its first. */
  private final int column;
  private final int row;
  /** The row of the list the walk is on, and the cell of its scroll bar. */
  private int rowsWalked;
  /** Whether the click fell on the list's scroll bar, which then takes it instead of the row. */
  private boolean onBarLine;
  /** What the click does on the list's scroll bar: nothing, unless it fell on the bar. */
  private ViewVisitor onBar = new ViewVisitor() {
  };
  /** How many options of a group the walk has met. */
  private int options;

  /** Follows the drag that comes after a press on a list's thumb, in cells counted from the list's first. */
  private final Grab grab;

  ClickFocused(int column, int row, Settle pending, Grab grab) {
    this.pending = pending;
    this.grab = grab;
    this.column = column;
    this.row = row;
  }

  @Override
  public void visitButton(Button button) {
    pending.note(button::hold);
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    pending.note(box::toggle);
  }

  /** Where a list's scroll bar is, counted from the list's first column. */
  @Override
  public void visitListBox(ListBox box) {
    BarLine line = new TextBarLine(new TextPoint(box.width() - 1, 0), new TextPoint(0, 1));
    onBarLine = line.holds(new TextPoint(column, row));
    if (onBarLine) {
      onBar = new BarClick(row, pending, line, grab);
    }
  }

  /** An item is selected wherever its row is clicked, unless the bar's column takes the click. */
  @Override
  public void visitListItem(ListItem item) {
    onRow(item);
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    onRow(item);
  }

  @Override
  public void visitScrollUp(ScrollBar bar) {
    onBar.visitScrollUp(bar);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    onBar.visitScrollTrack(bar);
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    onBar.visitScrollThumb(bar);
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    onBar.visitScrollDown(bar);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    onBar.visitNoScrollBar(bar);
  }

  @Override
  public void visitNoListItem(ListBox box) {
    rowsWalked++;
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    pending.note(dropDown::down);
  }

  @Override
  public void visitOption(Option option) {
    onOption(option);
  }

  @Override
  public void visitChosenOption(Option option) {
    onOption(option);
  }


  /** Notes selecting the item on this row, when it is the one clicked and the click is off the bar's column. */
  private void onRow(ListItem item) {
    if (rowsWalked++ == row && !onBarLine) {
      pending.note(item::select);
    }
  }

  /** Notes choosing the option on this row, when it is the one clicked; options take one row each. */
  private void onOption(Option option) {
    if (options++ == row) {
      pending.note(option::choose);
    }
  }
}
