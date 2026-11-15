package com.fpetrola.chuin.text.pointer;

import com.fpetrola.chuin.geometry.Point;


/**
 * Where a scroll bar lies on the screen, down or across: the drawing of its cells, a click on one of them and the drag
 * of its thumb all count cells along the same line.
 */
public interface BarLine {
  /** Whether a cell is on the bar's column, for a bar down, or on its row, for one across, wherever along it. */
  boolean holds(Point cell);

  /** How far along the bar a cell is, counted from its first. */
  int along(Point cell);

  /** The cell this many cells along the bar. */
  Point cell(int along);
}
