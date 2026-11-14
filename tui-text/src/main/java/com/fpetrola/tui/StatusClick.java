package com.fpetrola.tui;

/** Runs the entry of the status line that covers a column of the bottom row, if any does. */
class StatusClick implements StatusVisitor {
  private final int clicked;
  private int column;

  StatusClick(int clicked) {
    this.clicked = clicked;
  }

  @Override
  public void visitStatusItem(StatusItem item) {
    int width = new TextCaption(item.text()).shown().length() + 2;
    if (clicked >= column && clicked < column + width) {
      item.run();
    }
    column += width;
  }
}
