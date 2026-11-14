package com.fpetrola.tui;

/** Closes the open list of the drop down it meets, telling whoever asked that something was closed. */
class CloseOpenList implements ViewVisitor {
  private final Runnable closed;

  CloseOpenList(Runnable closed) {
    this.closed = closed;
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    item.dropDown().close();
    closed.run();
  }
}
