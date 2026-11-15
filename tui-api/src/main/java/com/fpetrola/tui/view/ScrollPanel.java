package com.fpetrola.tui.view;

import com.fpetrola.tui.geometry.Point;


/**
 * A view of a size of its own that holds another, which may be bigger, and shows part of it from an origin. Its last
 * column carries a scroll bar for rows and its last row one for columns, which show and move the origin. Visiting it
 * enters it, goes on to what it holds, and leaves it.
 */
public interface ScrollPanel extends View {
  /** The place of what it holds that shows at its top left; 0, 0 until it is scrolled. */
  Point origin();

  /** Moves what shows by these many cells to the right and down, never before 0, 0 nor past what it holds. */
  void scroll(int columns, int rows);

  /** The bar down its last column, which scrolls by rows. */
  ScrollBar vertical();

  /** The bar across its last row, which scrolls by columns. */
  ScrollBar horizontal();
}
