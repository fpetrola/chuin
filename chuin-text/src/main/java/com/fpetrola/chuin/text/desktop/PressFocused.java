package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.ViewVisitor;



/** What Enter does: presses a button. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class PressFocused implements ViewVisitor {
  @Override
  public void visitButton(Button button) {
    button.press();
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    dropDown.choose();
  }
}
