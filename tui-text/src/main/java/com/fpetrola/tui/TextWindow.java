package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextWindow implements Window {
  private final String title;
  private final List<View> views = new ArrayList<>();
  private final List<Point> positions = new ArrayList<>();
  private Area area = new TextArea(0, 0, 0, 0);

  TextWindow(String title) {
    this.title = title;
  }

  @Override
  public void add(View view, Point position) {
    views.add(view);
    positions.add(position);
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
  public void acceptChildren(ChildVisitor visitor) {
    for (int at = 0; at < views.size(); at++) {
      visitor.child(views.get(at), positions.get(at));
    }
  }
}
