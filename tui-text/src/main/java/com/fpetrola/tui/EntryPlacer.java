package com.fpetrola.tui;

/**
 * Puts entries in a row left to right, each starting where the one before ends: the titles of the menu bar and the
 * entries of the status line, alike for drawing them and for finding the one under a click.
 */
interface EntryPlacer {
  /** Where the next entry, with this caption, goes; the row moves on past it. */
  PlacedEntry place(Caption caption);
}
