package com.fpetrola.tui;

public interface Window {
  void add(View view, Point position);

  void place(Area area);

  void accept(ViewVisitor visitor);
}
