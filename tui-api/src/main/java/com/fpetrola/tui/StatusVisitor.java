package com.fpetrola.tui;

/** Visits a status line: the line first, then each entry, left to right. */
interface StatusVisitor {
  default void visitStatusLine(StatusLine line) {
  }

  default void visitStatusItem(StatusItem item) {
  }
}
