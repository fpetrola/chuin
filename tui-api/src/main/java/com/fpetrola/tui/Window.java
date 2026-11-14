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
}
