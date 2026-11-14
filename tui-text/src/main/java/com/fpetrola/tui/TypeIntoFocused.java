package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** Types a character into the input line that has the focus, if the focused view is one. */
class TypeIntoFocused implements ViewVisitor {
  private final int focused;
  private final char typed;
  private int seen;

  TypeIntoFocused(int focused, char typed) {
    this.focused = focused;
    this.typed = typed;
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
    seen++;
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type) {
    if (seen == focused) {
      type.accept(typed);
    }
    seen++;
  }
}
