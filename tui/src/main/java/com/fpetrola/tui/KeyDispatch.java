package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

/** Delivers a typed key to the focused view: Enter presses the first button of a window. */
class KeyDispatch implements ViewVisitor {
  private final Key typed;
  private final Key enter;
  private final List<Button> pressed = new ArrayList<>();

  KeyDispatch(Key typed, Key enter) {
    this.typed = typed;
    this.enter = enter;
  }

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    if (typed == enter && pressed.isEmpty()) {
      pressed.add(button);
      listeners.forEach(listener -> listener.pressed(button));
    }
  }
}
