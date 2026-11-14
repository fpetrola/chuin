package com.fpetrola.tui;

/** Tells keys apart without asking their type: each key says which one it is. */
interface KeyVisitor {
  void enter();

  void tab();

  void space();

  void character(char typed);

  void backspace();

  void up();

  void down();

  void left();

  void right();

  /** A function key, F1 to F12, by its number. */
  void function(int number);

  void other();
}
