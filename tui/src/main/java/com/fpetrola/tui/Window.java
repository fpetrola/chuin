package com.fpetrola.tui;

public interface Window {
  void add(Button button);

  void add(Button button, Point position);

  void place(Area area);

  void accept(ViewVisitor visitor);
}
