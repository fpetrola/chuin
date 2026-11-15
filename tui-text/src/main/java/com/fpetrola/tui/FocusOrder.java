package com.fpetrola.tui;

/**
 * The order views take the focus in, as a walk meets them: each focusable view met is the next one, and one of them,
 * counted from the first, is the focused one.
 */
interface FocusOrder {
  /** Counts the focusable view the walk has just met, and tells whether it is the focused one. */
  boolean next();
}
