package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;

/** What a drag does each time the pointer moves with the button held, as moving a window or a scroll bar's thumb. */
interface Dragging {
  void to(Point cell);
}
