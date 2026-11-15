package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.Placement;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.ArrayList;
import java.util.List;

/** Views placed together: visiting it goes on to each placement; it is as big as reaches its farthest view. */
class TextGroup implements Group {
  /** The placements it is made of, in the order they were added. */
  private final List<Placement> placements = new ArrayList<>();

  @Override
  public void add(View view, Point position) {
    placements.add(new TextPlacement(view, position));
  }

  @Override
  public void accept(ViewVisitor visitor) {
    placements.forEach(placement -> placement.accept(visitor));
  }

  @Override
  public Size size() {
    int columns = 0;
    int rows = 0;
    for (Placement placement : placements) {
      Area area = placement.area();
      columns = Math.max(columns, area.column() + area.columns());
      rows = Math.max(rows, area.row() + area.rows());
    }
    return new TextSize(columns, rows);
  }
}
