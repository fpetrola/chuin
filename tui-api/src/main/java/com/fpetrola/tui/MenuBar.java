package com.fpetrola.tui;

/** The row across the top of the desktop that holds its menus, left to right. */
public interface MenuBar {
  void add(Menu menu);

  /** Visits the bar, then each of its menus, which go on to their items. */
  void accept(MenuVisitor visitor);
}
