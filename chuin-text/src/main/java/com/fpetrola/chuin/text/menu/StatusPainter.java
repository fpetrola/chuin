package com.fpetrola.chuin.text.menu;

import com.fpetrola.chuin.text.view.TextCaption;
import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.menu.StatusItem;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.menu.StatusVisitor;
import com.fpetrola.chuin.screen.Screen;


/** Draws the status line across the bottom row: each entry with a blank on each side, its marked key highlighted. */
class StatusPainter implements StatusVisitor {
  private final Screen screen;
  /** The bottom row, which takes the entries left to right. */
  private final EntryRow entries;

  StatusPainter(Screen screen) {
    this.screen = screen;
    this.entries = new TextEntryRow(screen, screen.lines().size() - 1);
  }

  @Override
  public void visitStatusLine(StatusLine line) {
    int columns = screen.lines().getFirst().length();
    int row = screen.lines().size() - 1;
    screen.write(0, row, " ".repeat(columns));
    TintRole.MENU.paint(screen, 0, row, columns);
  }

  @Override
  public void visitStatusItem(StatusItem item) {
    entries.put(new TextCaption(item.text()), TintRole.MENU, TintRole.LETTER);
  }
}
