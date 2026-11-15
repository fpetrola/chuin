package com.fpetrola.chuin.text.pointer;

import com.fpetrola.chuin.geometry.Point;

/** Something drawn on the screen that a click may fall on. */
public interface ClickTarget {
  /** Does what a click on this cell does to it, or, when the click does not fall on it, runs otherwise. */
  void click(Point cell, Runnable otherwise);
}
