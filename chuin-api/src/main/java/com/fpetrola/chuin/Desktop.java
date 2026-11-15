package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;


public interface Desktop {
  void open(Window window);

  /** Shows this menu bar across the top row, over the windows. */
  void show(MenuBar bar);

  /** Shows this status line across the bottom row, over the windows, and binds the key of each entry to it. */
  void show(StatusLine status);

  void type(Key key);

  /**
   * Runs the command whenever this key is typed, whatever view has the focus or menu is open, closing the menu; the
   * key then reaches nothing else. A binding wins over what the key did before, F10 and Tab included; binding a key
   * again replaces its command.
   */
  void bind(Key key, Runnable command);

  /** The left mouse button pressed on this screen cell, counted from 0. */
  void click(Point cell);

  /** The pointer moved to this cell with the left button still held since a click. */
  void drag(Point cell);

  /** The left button let go on this cell. */
  void release(Point cell);

  void draw(Screen screen);
}
