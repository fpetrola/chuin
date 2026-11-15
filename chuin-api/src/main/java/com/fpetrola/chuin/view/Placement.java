package com.fpetrola.chuin.view;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;


/** Where a view sits inside its window; visiting it goes on to the view. */
public interface Placement {
  /** Its position inside the window, counted from the top left cell inside the frame. */
  Point position();

  /** Where its view sits and how big the view is, counted as its position is. */
  Area area();

  void accept(ViewVisitor visitor);
}
