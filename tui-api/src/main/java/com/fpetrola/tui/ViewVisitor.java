package com.fpetrola.tui;

import java.util.List;

/** Visits views without asking their type: each view hands over what it holds. */
interface ViewVisitor {
  void window(String title, Area area);

  /** The next view visited sits at this position inside the window being visited. */
  void child(Point position);

  void button(Button button, String caption, List<ButtonListener> listeners);

  void label(String text);

  /** A check box hands over its text, its mark, and how to toggle it. */
  void checkBox(CheckBox box, String text, Mark mark, Runnable toggle);
}
