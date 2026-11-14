package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** What a printable key does: types its character into an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class TypeIntoFocused implements ViewVisitor {
  private final char typed;

  TypeIntoFocused(char typed) {
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
  }

  @Override
  public void label(String text) {
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
    type.accept(typed);
  }
}
