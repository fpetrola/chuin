package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.view.ScrollPanel;
import com.fpetrola.tui.view.View;
import com.fpetrola.tui.view.ViewVisitor;
import com.fpetrola.tui.view.Window;


class TextWindow implements Window {
  private final String title;
  /** The views placed in it. */
  private final Group group = new TextGroup();
  private Area area = new TextArea(0, 0, 0, 0);
  /** Its inside, which shows its views from an origin, with its bars on the right and bottom of the frame. */
  private final ScrollPanel panel = new TextScrollPanel(group, () -> new TextSize(area.columns() - 1, area.rows() - 1));

  TextWindow(String title) {
    this.title = title;
  }

  @Override
  public void add(View view, Point position) {
    group.add(view, position);
  }

  @Override
  public void place(Area area) {
    this.area = area;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitWindow(this);
    panel.accept(visitor);
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public Area area() {
    return area;
  }
}
