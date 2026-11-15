package com.fpetrola.tui.menu;

/** Visits a menu bar: the bar first, then each menu left to right, each followed by its items top to bottom. */
public interface MenuVisitor {
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
