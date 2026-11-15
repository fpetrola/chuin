package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.ViewVisitor;



/** What a printable key does: types its character into an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class TypeIntoFocused implements ViewVisitor {
  private final char typed;

  TypeIntoFocused(char typed) {
    this.typed = typed;
  }

  @Override
  public void visitInputLine(InputLine line) {
    line.type(typed);
  }
}
