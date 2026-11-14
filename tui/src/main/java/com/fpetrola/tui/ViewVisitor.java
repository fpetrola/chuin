package com.fpetrola.tui;

import java.util.List;

/** Visits views without asking their type: each view hands over what it holds. */
interface ViewVisitor {
  void window(String title, Area area);

  /** The next view visited sits at this position inside the window being visited. */
  void child(Point position);

  void button(Button button, String caption, List<ButtonListener> listeners);
}
