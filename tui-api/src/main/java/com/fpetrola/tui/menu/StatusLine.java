package com.fpetrola.tui.menu;

import com.fpetrola.tui.input.Key;


/** The row across the bottom of the desktop: entries that each show a key and run a command on it or on a click. */
public interface StatusLine {
  /** Adds an entry to the right of the others: its text, the key that runs it, and the command. */
  void add(String text, Key key, Runnable command);

  /** Visits the line, then each of its entries, left to right. */
  void accept(StatusVisitor visitor);
}
