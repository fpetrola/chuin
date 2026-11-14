package com.fpetrola.tui;

import java.util.List;

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
  public void visitInputLine(InputLine line) {
    into.add(line);
  }

  @Override
  public void listBox(ListBox box, List<String> items, int rows, int top, int selected, Runnable previous, Runnable next) {
    into.add(box);
  }

  @Override
  public void radioButtons(RadioButtons group, List<String> options, int chosen, Runnable previous, Runnable next) {
    into.add(group);
  }
}
