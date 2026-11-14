package com.fpetrola.tui;

/** Meets each menu of a menu bar. */
interface MenuVisitor {
  void visitMenu(Menu menu);
}
