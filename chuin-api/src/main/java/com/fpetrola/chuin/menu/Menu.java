package com.fpetrola.chuin.menu;

import com.fpetrola.chuin.input.Key;


/** A menu of the menu bar, known by its title, with items that run a command when chosen. */
public interface Menu {
  String title();

  /** Adds an item at the bottom of the menu, with the command it runs when chosen. */
  void add(String item, Runnable command);

  /** Adds an item that a key runs too, from anywhere once the bar is shown; the key's name is shown on its right. */
  void add(String item, Key key, Runnable command);

  /** Adds a line between the items above and below it; the arrows step over it. */
  void separator();

  /** How many cells across its items take in its open box: its longest item and a blank on each side; the frame around them is the border's. */
  int width();

  /** Visits the menu, then each of its items, top to bottom. */
  void accept(MenuVisitor visitor);
}
