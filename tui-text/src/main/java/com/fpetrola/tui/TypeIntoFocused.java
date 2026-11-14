package com.fpetrola.tui;

import java.util.function.Consumer;

/** What a printable key does: types its character into an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class TypeIntoFocused implements ViewVisitor {
  private final char typed;

  TypeIntoFocused(char typed) {
    this.typed = typed;
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
    type.accept(typed);
  }
}
