package com.fpetrola.tui;

import java.util.List;

/** Toggles the check box that has the focus: the one at the focused place among a window's focusable views. */
class ToggleFocused implements ViewVisitor {
  private final int focused;
  private int seen;

  ToggleFocused(int focused) {
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
    seen++;
  }

  @Override
  public void label(String text) {
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    if (seen == focused) {
      toggle.run();
    }
    seen++;
  }
}
