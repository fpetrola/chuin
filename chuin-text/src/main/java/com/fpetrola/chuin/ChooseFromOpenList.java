package com.fpetrola.chuin;

import com.fpetrola.chuin.text.pointer.Settle;
import com.fpetrola.chuin.view.DropDownItem;
import com.fpetrola.chuin.view.ViewVisitor;


/**
 * A click that may fall on an item of the focused drop down's open list, which is drawn under its field inside a frame.
 * The item under the click is chosen once the walk is over; when the click is on none, what it does otherwise runs.
 */
class ChooseFromOpenList implements ViewVisitor {
  /** What the click or key does once the walk is over. */
  private final Settle pending;
  /** The frame's top line between the field and the first item. */
  private static final int ABOVE_ITEMS = 2;
  /** Which column and row of the drop down's field was clicked, counted from its first. */
  private final int column;
  private final int row;
  /** The row of the open list the walk is on. */
  private int walked;

  ChooseFromOpenList(int column, int row, Settle pending) {
    this.pending = pending;
    this.column = column;
    this.row = row;
  }

  @Override
  public void visitDropDownItem(DropDownItem item) {
    onItem(item);
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    onItem(item);
  }


  /** Notes this item as the one to choose when the click is on its row, between the frame's sides. */
  private void onItem(DropDownItem item) {
    boolean across = column >= 1 && column <= item.dropDown().width();
    if (walked++ == row - ABOVE_ITEMS && across) {
      pending.note(item::choose);
    }
  }
}
