package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;


/** The windows of a desktop, without its menu bar: they open, take keys and clicks, and draw themselves. */
public interface Windows {
  void open(Window window);

  void type(Key key);

  /** The left mouse button pressed on this screen cell, counted from 0. */
  void click(Point cell);

  /** The pointer moved to this cell with the left button held since a click. */
  void drag(Point cell);

  /** The left button let go on this cell. */
  void release(Point cell);

  void draw(Screen screen);

  /** Closes whatever is open in the windows, such as a drop down's list; when nothing is open, runs otherwise instead. */
  void escape(Runnable otherwise);
}
