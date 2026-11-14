package com.fpetrola.tui;

import java.util.Optional;

/** A field showing one chosen item of a list, which opens under it to choose another; visiting it goes on to its items while open. */
public interface DropDown extends View {
  /** The item chosen now, or nothing when it has no items. */
  Optional<String> chosen();

  /** How many cells its field takes, its longest item, without the arrow. */
  int width();

  /** Opens the list with the chosen item highlighted, or highlights the next item once open, staying on the last. */
  void down();

  /** Highlights the item before once open, staying on the first; does nothing while closed. */
  void up();

  /** Chooses the highlighted item and closes the list; does nothing while closed. */
  void choose();

  /** Closes the list and keeps the item chosen before it opened. */
  void close();
}
