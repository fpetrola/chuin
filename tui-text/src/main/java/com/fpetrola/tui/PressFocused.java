package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** Presses the button that has the focus: the one at the focused place among a window's focusable views. */
class PressFocused implements ViewVisitor {
  private final int focused;
  private int seen;

  PressFocused(int focused) {
    this.focused = focused;
  }

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    if (seen == focused) {
      listeners.forEach(listener -> listener.pressed(button));
    }
    seen++;
  }

  @Override
  public void label(String text) {
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    seen++;
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type) {
    seen++;
  }
}
