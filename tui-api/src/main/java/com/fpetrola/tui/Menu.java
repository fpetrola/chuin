package com.fpetrola.tui;

/** A menu of the menu bar, known by its title, with items that run a command when chosen. */
public interface Menu {
  String title();

  /** Adds an item at the bottom of the menu, with the command it runs when chosen. */
  void add(String item, Runnable command);

  /** Walks its items, top to bottom. */
  void acceptItems(MenuItemVisitor visitor);
}
