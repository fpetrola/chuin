package com.fpetrola.chuin.text.menu;

import com.fpetrola.chuin.text.view.Caption;
import com.fpetrola.chuin.text.screen.Tint;

/**
 * A row of the screen that takes entries left to right, each its caption with a blank on each side, its marked letters
 * highlighted, the column moving on past it: the titles of the menu bar and the entries of the status line.
 */
interface EntryRow {
  /** Draws the next entry in a tint, its marked letters in another, and moves past it. */
  void put(Caption caption, Tint tint, Tint letter);
}
