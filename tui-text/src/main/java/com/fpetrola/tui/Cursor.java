package com.fpetrola.tui;

/**
 * Which one of a fixed number of places is the current one, kept among them: the selected item of a list, the chosen
 * option of a group, the item of an open menu. Moving past either end stops at it.
 */
interface Cursor {
  /** The current place, counted from the first. */
  int at();

  /** Moves this many places on, or back when negative, stopping at the first or the last. */
  void step(int places);

  /** Moves to this place, or to the first or the last when it falls before or after them. */
  void moveTo(int place);
}
