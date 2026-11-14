package com.fpetrola.tui;

/** The row across the top of the desktop that holds its menus, left to right. */
public interface MenuBar {
  void add(Menu menu);

  /** Walks its menus, left to right. */
  void acceptMenus(MenuVisitor visitor);
}
