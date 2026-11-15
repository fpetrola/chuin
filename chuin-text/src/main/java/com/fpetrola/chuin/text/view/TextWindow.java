package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.view.ScrollPanel;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;
import com.fpetrola.chuin.view.Window;


public class TextWindow implements Window {
  private final String title;
  /** The views placed in it. */
  private final Group group = new TextGroup();
  private Area area = new TextArea(0, 0, 0, 0);
  /** Its inside, which shows its views from an origin, with its bars on the right and bottom of the frame. */
  private final ScrollPanel panel = new TextScrollPanel(group, () -> new TextSize(area.columns() - 1, area.rows() - 1));

  public TextWindow(String title) {
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
