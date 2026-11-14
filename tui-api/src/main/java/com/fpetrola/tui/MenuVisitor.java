package com.fpetrola.tui;

/** Visits a menu bar: the bar first, then each menu left to right, each followed by its items top to bottom. */
interface MenuVisitor {
  default void visitMenuBar(MenuBar bar) {
  }

  default void visitMenu(Menu menu) {
  }

  default void visitMenuItem(MenuItem item) {
  }

  /** A line between items of this menu. */
  default void visitMenuSeparator(Menu menu) {
  }
}
