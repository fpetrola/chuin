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
  public void add(Button button) {
    add(button, new TextPoint(0, 0));
  }

  @Override
  public void add(Button button, Point position) {
    children.add(visitor -> {
      visitor.child(position);
      button.accept(visitor);
    });
  }

  @Override
  public void add(Label label, Point position) {
    children.add(visitor -> {
      visitor.child(position);
      label.accept(visitor);
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
