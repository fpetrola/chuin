package com.fpetrola.tui;

interface MarkVisitor {
  default void checked() {
  }

  default void unchecked() {
  }
}
