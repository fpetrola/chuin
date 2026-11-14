package com.fpetrola.tui;

/** Tells keys apart without asking their type: each key says which one it is. */
interface KeyVisitor {
  default void enter() {
  }

  default void tab() {
  }

  default void space() {
  }

  default void character(char typed) {
  }

  default void backspace() {
  }

  default void up() {
  }

  default void down() {
  }

  default void left() {
  }

  default void right() {
  }

  /** Ctrl held with a letter, by the letter in lower case; Tab, Enter, Ctrl+Q and Ctrl+H (backspace) are not handed over this way. */
  default void control(char letter) {
  }

  /** A function key, F1 to F12, by its number. */
  default void function(int number) {
  }

  default void other() {
  }

  /** The escape key alone, with nothing right after it. */
  default void escape() {
  }

  /** Alt held with a letter, as terminals send it: an escape right before the letter. */
  default void alt(char letter) {
  }
}
