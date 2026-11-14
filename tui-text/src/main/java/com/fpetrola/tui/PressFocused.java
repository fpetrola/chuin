package com.fpetrola.tui;

import java.util.List;

/** What Enter does: presses a button. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class PressFocused implements ViewVisitor {
  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    listeners.forEach(listener -> listener.pressed(button));
  }
}
