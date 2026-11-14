package com.fpetrola.tui;

/** A menu of the menu bar, known by its title, with items that run a command when chosen. */
public interface Menu {
  String title();

  /** Adds an item at the bottom of the menu, with the command it runs when chosen. */
  void add(String item, Runnable command);

  /** Adds an item that a key runs too, from anywhere once the bar is shown; the key's name is shown on its right. */
  void add(String item, Key key, Runnable command);

  /** Adds a line between the items above and below it; the arrows step over it. */
  void separator();

  /** How many cells across its open box takes: its longest item, a blank on each side and the two borders. */
  int width();

  /** Visits the menu, then each of its items, top to bottom. */
  void accept(MenuVisitor visitor);
}
