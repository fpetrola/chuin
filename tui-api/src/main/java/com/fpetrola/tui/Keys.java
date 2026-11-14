package com.fpetrola.tui;

public interface Keys {
  Key enter();

  Key tab();

  Key space();

  /** The function key F1 to F12 by its number, equal to the one the keyboard reads, so it can be bound. */
  Key function(int number);

  /** The escape key alone. */
  Key escape();
}
