package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** Collects the views that can take the focus, in the order they are visited. */
class CollectFocusable implements ViewVisitor {
  private final List<View> into;

  CollectFocusable(List<View> into) {
    this.into = into;
  }

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    into.add(button);
  }

  @Override
  public void label(String text) {
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    into.add(box);
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type) {
    into.add(line);
  }
}
