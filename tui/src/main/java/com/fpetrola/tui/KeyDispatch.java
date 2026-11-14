package com.fpetrola.tui;

import java.util.List;

/** Delivers a typed key to the focused view: the first button of a window. */
class KeyDispatch implements ViewVisitor {
  @Override
  public void window(List<Button> buttons) {
    buttons.stream().findFirst().ifPresent(focused -> focused.accept(this));
  }

  @Override
  public void button(Button button, List<ButtonListener> listeners) {
    listeners.forEach(listener -> listener.pressed(button));
  }
}
