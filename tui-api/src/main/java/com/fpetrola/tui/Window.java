package com.fpetrola.tui;

public interface Window {
  void add(View view, Point position);

  void place(Area area);

  void accept(ViewVisitor visitor);

  /** The title on its top border. */
  String title();

  /** Where it is placed on the screen. */
  Area area();

  /** Has the visitor visit each view placed in it, in the order they were added. */
  void acceptViews(ViewVisitor visitor);

  /** Walks the views placed in it, in the order they were added, each with its position inside the window. */
  void acceptChildren(ChildVisitor visitor);
}
