package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Area;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.DropDownItem;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * The open list of a drop down, gathered as its items are visited and drawn last, over everything: a raised box under
 * the field, the highlighted item bright. A closed drop down visits no items, so its list draws nothing.
 */
class DropDownList implements ViewVisitor, Drawing {
  private final int column;
  private final int row;
  private final int width;
  private final List<String> items = new ArrayList<>();
  private int highlighted;

  DropDownList(int column, int row, int width) {
    this.column = column;
    this.row = row;
    this.width = width;
  }

  @Override
  public void visitDropDownItem(DropDownItem item) {
    items.add(item.text());
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    highlighted = items.size();
    items.add(item.text());
  }

  @Override
  public void drawOn(Screen screen) {
    if (items.isEmpty()) {
      return;
    }
    Area held = Line.SINGLE.holding(new TextPoint(column, row), new TextSize(width, items.size()));
    new RaisedFrame(Line.SINGLE.around(held), "", TintRole.MENU, Line.SINGLE).drawOn(screen);
    for (int at = 0; at < items.size(); at++) {
      screen.write(held.column(), held.row() + at, items.get(at));
    }
    TintRole.FOCUS.paint(screen, held.column(), held.row() + highlighted, width);
  }
}
