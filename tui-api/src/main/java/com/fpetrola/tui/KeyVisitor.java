package com.fpetrola.tui;

/** Tells keys apart without asking their type: each key says which one it is. */
interface KeyVisitor {
  void enter();

  void tab();

  void space();

  void other();
}
