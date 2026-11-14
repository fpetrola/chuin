package com.fpetrola.tui;

/** A group of options of which exactly one is chosen; visiting it goes on to each option. */
public interface RadioButtons extends View {
  /** How many cells its longest option takes, mark included. */
  int width();


  /** Chooses the option before, staying on the first. */
  void previous();

  /** Chooses the option after, staying on the last. */
  void next();

  /** Chooses the option in this place, counted from 0; does nothing for a place it does not have. */
  void choose(int option);
}
