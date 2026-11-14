package com.fpetrola.tui;

/** Tells apart the options of a group of radio buttons: the chosen one and the rest. */
interface OptionVisitor {
  default void option(String text) {
  }

  default void chosenOption(String text) {
  }
}
