package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;

/** Something drawn on the screen that a click may fall on. */
interface ClickTarget {
  /** Does what a click on this cell does to it, or, when the click does not fall on it, runs otherwise. */
  void click(Point cell, Runnable otherwise);
}
