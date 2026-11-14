package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** Places a window a few cells away from where it says it is. */
class MoveWindow implements ViewVisitor {
  private final Window moved;
  private final int columns;
  private final int rows;

  MoveWindow(Window moved, int columns, int rows) {
    this.moved = moved;
    this.columns = columns;
    this.rows = rows;
  }

  @Override
  public void window(String title, Area area) {
    moved.place(new TextArea(area.column() + columns, area.row() + rows, area.columns(), area.rows()));
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
  }

  @Override
  public void listBox(ListBox box, List<String> items, int rows, int top, int selected, Runnable previous, Runnable next) {
  }
}
