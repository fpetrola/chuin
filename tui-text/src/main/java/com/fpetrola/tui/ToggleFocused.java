package com.fpetrola.tui;

import com.fpetrola.tui.view.CheckBox;
import com.fpetrola.tui.view.InputLine;
import com.fpetrola.tui.view.ViewVisitor;



/** What Space does: toggles a check box, types a blank into an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class ToggleFocused implements ViewVisitor {
  @Override
  public void visitCheckBox(CheckBox box) {
    box.toggle();
  }

  @Override
  public void visitInputLine(InputLine line) {
    line.type(' ');
  }
}
