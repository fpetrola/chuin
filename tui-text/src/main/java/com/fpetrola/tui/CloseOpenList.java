package com.fpetrola.tui;

/**
 * Closes the open list of the drop down it meets. What to do when it closes nothing is kept until the visit is over:
 * closing a list drops it, and settling runs it if it is still due.
 */
class CloseOpenList implements ViewVisitor, Settle {
  private static final Runnable NOTHING = () -> { };
  private Runnable due;

  /** Closes an open list and does nothing else. */
  CloseOpenList() {
    this(NOTHING);
  }

  /** Closes an open list, or runs otherwise once the visit is over if there was none. */
  CloseOpenList(Runnable otherwise) {
    this.due = otherwise;
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    item.dropDown().close();
    due = NOTHING;
  }

  @Override
  public void settle() {
    due.run();
  }
}
