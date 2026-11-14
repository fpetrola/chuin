package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** What Space does to the focused view: toggles a check box, types a blank into an input line. */
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

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type) {
    if (seen == focused) {
      type.accept(' ');
    }
    seen++;
  }
}
