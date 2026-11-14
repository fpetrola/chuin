package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class TextWindow implements Window {
  private final String title;
  /** Each view placed in it, already paired with its position, so the two can never fall out of step. */
  private final List<Consumer<ChildVisitor>> children = new ArrayList<>();
  private Area area = new TextArea(0, 0, 0, 0);

  TextWindow(String title) {
    this.title = title;
  }

  @Override
  public void add(View view, Point position) {
    children.add(visitor -> visitor.child(view, position));
  }

  @Override
  public void place(Area area) {
    this.area = area;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitWindow(this);
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public Area area() {
    return area;
  }

  @Override
  public void acceptViews(ViewVisitor visitor) {
    acceptChildren((view, position) -> view.accept(visitor));
  }

  @Override
  public void acceptChildren(ChildVisitor visitor) {
    children.forEach(child -> child.accept(visitor));
  }
}
