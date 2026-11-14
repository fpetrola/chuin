package com.fpetrola.tui;

/** The windows of a desktop, without its menu bar: they open, take keys and clicks, and draw themselves. */
interface Windows {
  void open(Window window);

  void type(Key key);

  /** The left mouse button pressed on this screen cell, counted from 0. */
  void click(Point cell);

  void draw(Screen screen);

  /** Closes whatever is open in the windows, such as a drop down's list; when nothing is open, runs otherwise instead. */
  void escape(Runnable otherwise);
}
