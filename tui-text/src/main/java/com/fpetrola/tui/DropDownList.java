package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

/**
 * The open list of a drop down, gathered as its items are visited and drawn last, over everything: a raised box under
 * the field, the highlighted item bright. A closed drop down visits no items, so its list draws nothing.
 */
class DropDownList implements ViewVisitor, Drawing {
  /** An open list, like an open menu: black on grey. */
  private static final char TINT = 'm';
  private static final char HIGHLIGHT = 'f';
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
    new RaisedFrame(new TextArea(column, row, width + 2, items.size() + 2), "", TINT, Lines.SINGLE).drawOn(screen);
    for (int at = 0; at < items.size(); at++) {
      screen.write(column + 1, row + 1 + at, items.get(at));
    }
    screen.paint(column + 1, row + 1 + highlighted, width, HIGHLIGHT);
  }
}
