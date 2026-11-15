package com.fpetrola.tui.menu;

/** Visits a status line: the line first, then each entry, left to right. */
public interface StatusVisitor {
  default void visitStatusLine(StatusLine line) {
  }

  default void visitStatusItem(StatusItem item) {
  }
}
