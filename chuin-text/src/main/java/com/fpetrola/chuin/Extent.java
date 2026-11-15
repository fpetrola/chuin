package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;

/** What an area spans on the screen: which cells fall in it, its part another area shows, and a cell counted from its corner. */
interface Extent {
  /** Whether a cell falls in the area. */
  boolean covers(Point cell);

  /** The part of the area that falls within another; no cells when none does. */
  Area within(Area shown);

  /** A cell of the screen counted from the area's top left corner instead. */
  Point from(Point cell);
}
