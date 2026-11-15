package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.StatusItem;
import com.fpetrola.chuin.menu.StatusVisitor;


/** Runs the entry of the status line that covers a column of the bottom row, if any does. */
class StatusClick implements StatusVisitor {
  private final int clicked;
  private final EntryPlacer placer = new TextEntryPlacer();

  StatusClick(int clicked) {
    this.clicked = clicked;
  }

  @Override
  public void visitStatusItem(StatusItem item) {
    if (placer.place(new TextCaption(item.text())).covers(clicked)) {
      item.run();
    }
  }
}
