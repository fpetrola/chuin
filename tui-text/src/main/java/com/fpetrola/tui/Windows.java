package com.fpetrola.tui;

/** The windows of a desktop, without its menu bar: they open, take keys and clicks, and draw themselves. */
interface Windows {
  void open(Window window);

  void type(Key key);

  /** The left mouse button pressed on this screen cell, counted from 0. */
  void click(Point cell);

  void draw(Screen screen);
}
