package com.fpetrola.tui;

/** An entry of a row where the row puts it: from its first column, as wide as its caption takes as an entry. */
interface PlacedEntry {
  /** The column the entry starts at. */
  int start();

  /** Whether a column of the row falls on the entry. */
  boolean covers(int column);
}
