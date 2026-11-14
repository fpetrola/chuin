package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** What Space does: toggles a check box, types a blank into an input line. Meant to run inside {@link AtFocus}, so it only ever sees the focused view. */
class ToggleFocused implements ViewVisitor {

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
    toggle.run();
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
    type.accept(' ');
  }

  @Override
  public void listBox(ListBox box, List<String> items, int rows, int top, int selected, Runnable previous, Runnable next) {
  }
}
