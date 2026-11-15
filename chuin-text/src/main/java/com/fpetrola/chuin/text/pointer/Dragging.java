package com.fpetrola.chuin.text.pointer;

import com.fpetrola.chuin.geometry.Point;

/** What a drag does each time the pointer moves with the button held, as moving a window or a scroll bar's thumb. */
public interface Dragging {
  void to(Point cell);
}
