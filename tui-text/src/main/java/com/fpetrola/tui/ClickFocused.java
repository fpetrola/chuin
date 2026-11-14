package com.fpetrola.tui;


/** What a left click does to the view under it, once focused: presses a button, toggles a check box; any other view is only focused. */
class ClickFocused implements ViewVisitor {
  @Override
  public void visitButton(Button button) {
    button.press();
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    box.toggle();
  }
}
