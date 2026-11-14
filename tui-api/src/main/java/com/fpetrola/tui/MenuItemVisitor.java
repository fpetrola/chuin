package com.fpetrola.tui;

/** Meets each item of a menu, with the command it runs. */
interface MenuItemVisitor {
  void item(String text, Runnable command);
}
