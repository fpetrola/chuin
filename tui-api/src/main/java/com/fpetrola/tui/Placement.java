package com.fpetrola.tui;

/** Where a view sits inside its window; visiting it goes on to the view. */
public interface Placement {
  /** Its position inside the window, counted from the top left cell inside the frame. */
  Point position();

  void accept(ViewVisitor visitor);
}
