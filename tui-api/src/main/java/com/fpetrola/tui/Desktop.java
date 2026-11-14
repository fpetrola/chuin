package com.fpetrola.tui;

public interface Desktop {
  void open(Window window);

  /** Shows this menu bar across the top row, over the windows. */
  void show(MenuBar bar);

  void type(Key key);

  /** The left mouse button pressed on this screen cell, counted from 0. */
  void click(Point cell);

  void draw(Screen screen);
}
