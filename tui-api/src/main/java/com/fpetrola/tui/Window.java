package com.fpetrola.tui;

public interface Window {
  void add(View view, Point position);

  void place(Area area);

  /** Visits itself, then the scroll panel of its inside, which goes on to its placements and their views. */
  void accept(ViewVisitor visitor);

  /** The title on its top border. */
  String title();

  /** Where it is placed on the screen. */
  Area area();
}
