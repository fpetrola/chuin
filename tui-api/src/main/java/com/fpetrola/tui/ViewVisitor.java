package com.fpetrola.tui;

import java.util.List;

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

  default void visitInputLine(InputLine line) {
  }

  default void visitListBox(ListBox box) {
  }

  default void visitRadioButtons(RadioButtons group) {
  }
}
