package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.geometry.Size;

/** The lines a frame is drawn with, each row of it drawn by the border itself. */
interface Border {
  /** The top row: a corner, the line across with a label in the middle, a corner. */
  String top(String label, int inner);

  /** A row between the top and the bottom: the line down, a blank inside, the line down. */
  String middle(int inner);

  /** The bottom row: a corner, the line across, a corner. */
  String bottom(int inner);

  /** What a frame drawn around an area holds: the area less the border's cell on each side. */
  default Area inside(Area framed) {
    return new TextArea(framed.column() + 1, framed.row() + 1, framed.columns() - 2, framed.rows() - 2);
  }

  /** What a frame with its top left corner at this cell holds, when it holds this much: from the cell inside the corner on. */
  default Area holding(Point corner, Size held) {
    return new TextArea(corner.column() + 1, corner.row() + 1, held.columns(), held.rows());
  }

  /** The area a frame takes to hold this one: a cell more on each side for the border. */
  default Area around(Area held) {
    return new TextArea(held.column() - 1, held.row() - 1, held.columns() + 2, held.rows() + 2);
  }
}
