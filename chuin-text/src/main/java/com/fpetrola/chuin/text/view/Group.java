package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.view.View;


/** Views placed together, as a window's inside is: it keeps them, and visiting it goes on to each, in the order added. */
public interface Group extends View {
  void add(View view, Point position);
}
