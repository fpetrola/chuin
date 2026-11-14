package com.fpetrola.tui;

import java.util.List;

/** What a left click does to the view under it, once focused: presses a button, toggles a check box; any other view is only focused. */
class ClickFocused implements ViewVisitor {
  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    listeners.forEach(listener -> listener.pressed(button));
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    toggle.run();
  }
}
