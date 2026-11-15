package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.view.View;


/** Views placed together, as a window's inside is: it keeps them, and visiting it goes on to each, in the order added. */
interface Group extends View {
  void add(View view, Point position);
}
