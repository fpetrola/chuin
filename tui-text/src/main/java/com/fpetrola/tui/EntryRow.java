package com.fpetrola.tui;

/**
 * A row of the screen that takes entries left to right, each its caption with a blank on each side, its marked letters
 * highlighted, the column moving on past it: the titles of the menu bar and the entries of the status line.
 */
interface EntryRow {
  /** The column where the next entry starts. */
  int next();

  /** Draws the next entry in a tint, its marked letters in another, and moves past it. */
  void put(Caption caption, char tint, char letter);
}
