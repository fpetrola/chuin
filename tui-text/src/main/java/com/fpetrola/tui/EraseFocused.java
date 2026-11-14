package com.fpetrola.tui;

import java.util.function.Consumer;

/** What Backspace does: erases the last character of an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class EraseFocused implements ViewVisitor {
  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
    erase.run();
  }
}
