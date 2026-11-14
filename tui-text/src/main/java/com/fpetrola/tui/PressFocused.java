package com.fpetrola.tui;


/** What Enter does: presses a button. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class PressFocused implements ViewVisitor {
  @Override
  public void visitButton(Button button) {
    button.press();
  }
}
