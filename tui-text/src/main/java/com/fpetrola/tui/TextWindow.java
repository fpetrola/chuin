package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class TextWindow implements Window {
  private final String title;
  private final List<Consumer<ViewVisitor>> children = new ArrayList<>();
  private Area area = new TextArea(0, 0, 0, 0);

  TextWindow(String title) {
    this.title = title;
  }

  @Override
  public void add(View view, Point position) {
    children.add(visitor -> {
      visitor.child(position);
      view.accept(visitor);
    });
  }

  @Override
  public void place(Area area) {
    this.area = area;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.window(title, area);
    children.forEach(child -> child.accept(visitor));
  }
}
