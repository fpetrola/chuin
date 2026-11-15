package com.fpetrola.tui;

/**
 * Closes the open list of the drop down it meets. What to do when it closes nothing is kept until the visit is over:
 * closing a list drops it, and settling runs it if it is still due.
 */
class CloseOpenList implements ViewVisitor {
  /** What the click or key does once the walk is over. */
  private final Settle pending;

  /** Notes closing the open list it meets, in place of whatever else was due. */
  CloseOpenList(Settle pending) {
    this.pending = pending;
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    pending.note(item.dropDown()::close);
  }

}
