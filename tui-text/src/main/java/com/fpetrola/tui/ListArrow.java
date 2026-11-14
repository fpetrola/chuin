package com.fpetrola.tui;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

/** What ↑ or ↓ does to the focused view: a list selects the item before or after, any other view lets the window move. Every focusable view must be overridden here, or ↑ and ↓ do nothing while it has the focus. */
class ListArrow implements ViewVisitor {
  private final BinaryOperator<Runnable> choose;
  private final Runnable otherwise;

  ListArrow(BinaryOperator<Runnable> choose, Runnable otherwise) {
    this.choose = choose;
    this.otherwise = otherwise;
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    otherwise.run();
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    otherwise.run();
  }

  @Override
  public void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
    otherwise.run();
  }

  @Override
  public void listBox(ListBox box, List<String> items, int rows, int top, int selected, Runnable previous, Runnable next) {
    choose.apply(previous, next).run();
  }

  @Override
  public void radioButtons(RadioButtons group, List<String> options, int chosen, Runnable previous, Runnable next) {
    choose.apply(previous, next).run();
  }
}
