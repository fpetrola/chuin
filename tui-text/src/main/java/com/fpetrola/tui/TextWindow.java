package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextWindow implements Window {
  private final String title;
  private final List<Placement> placements = new ArrayList<>();
  private Area area = new TextArea(0, 0, 0, 0);
  private Point origin = new TextPoint(0, 0);

  TextWindow(String title) {
    this.title = title;
  }

  @Override
  public void add(View view, Point position) {
    placements.add(new TextPlacement(view, position));
  }

  @Override
  public void place(Area area) {
    this.area = area;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitWindow(this);
    placements.forEach(placement -> placement.accept(visitor));
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
  public Point origin() {
    return origin;
  }

  @Override
  public void scroll(int columns, int rows) {
    origin = new TextPoint(Math.max(0, origin.column() + columns), Math.max(0, origin.row() + rows));
  }
}
