package com.fpetrola.chuin;

import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.Placement;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;


class TextPlacement implements Placement {
  private final View view;
  private final Point position;

  TextPlacement(View view, Point position) {
    this.view = view;
    this.position = position;
  }

  @Override
  public Point position() {
    return position;
  }

  @Override
  public Area area() {
    Size size = view.size();
    return new TextArea(position.column(), position.row(), size.columns(), size.rows());
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitPlacement(this);
    view.accept(visitor);
  }
}
