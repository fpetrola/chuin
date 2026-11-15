package com.fpetrola.tui;

public interface Window {
  void add(View view, Point position);

  void place(Area area);

  /** Visits itself, then each of its placements, which go on to their views. */
  void accept(ViewVisitor visitor);

  /** The title on its top border. */
  String title();

  /** Where it is placed on the screen. */
  Area area();

  /** The place of what it holds that shows at the top left inside its frame; 0, 0 until it is scrolled. */
  Point origin();

  /** Moves what shows inside its frame by these many cells to the right and down; it never goes before 0, 0. */
  void scroll(int columns, int rows);
}
