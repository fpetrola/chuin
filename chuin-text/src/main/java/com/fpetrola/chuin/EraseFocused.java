package com.fpetrola.chuin;

import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.ViewVisitor;



/** What Backspace does: erases the last character of an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class EraseFocused implements ViewVisitor {
  @Override
  public void visitInputLine(InputLine line) {
    line.erase();
  }
}
