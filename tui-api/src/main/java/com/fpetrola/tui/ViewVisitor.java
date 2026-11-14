package com.fpetrola.tui;

import java.util.List;
import java.util.function.Consumer;

/** Visits views without asking their type: each view hands over what it holds. */
interface ViewVisitor {
  default void window(String title, Area area) {
  }

  /** The next view visited sits at this position inside the window being visited. */
  default void child(Point position) {
  }

  default void button(Button button, String caption, List<ButtonListener> listeners) {
  }

  default void label(String text) {
  }

  /** A check box hands over its text, its mark, and how to toggle it. */
  default void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
  }

  /** An input line hands over its text, its width in cells, where typed characters go, and how to erase the last one. */
  default void inputLine(InputLine line, String text, int width, Consumer<Character> type, Runnable erase) {
  }

  /** A list box hands over its items, how many rows it shows, the first one shown, the selected one, and how to select the one before or after. */
  default void listBox(ListBox box, List<String> items, int rows, int top, int selected, Runnable previous, Runnable next) {
  }
}
