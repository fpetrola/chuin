package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

/** Delivers a typed key to the focused view: the first button of a window. */
class KeyDispatch implements ViewVisitor {
  private final List<Button> pressed = new ArrayList<>();

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    if (pressed.isEmpty()) {
      pressed.add(button);
      listeners.forEach(listener -> listener.pressed(button));
    }
  }
}
