package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.view.View;


/** Views placed together, as a window's inside is: it keeps them, and visiting it goes on to each, in the order added. */
interface Group extends View {
  void add(View view, Point position);
}
