package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextMenu implements Menu {
  /** A blank on each side of an item and the two borders of the box. */
  private static final int AROUND_ITEM = 4;
  private final String title;
  private final List<MenuItem> items = new ArrayList<>();

  TextMenu(String title) {
    this.title = title;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public void add(String item, Runnable command) {
    items.add(new TextMenuItem(this, item, command));
  }

  @Override
  public int width() {
    return items.stream().mapToInt(item -> new TextCaption(item.text()).shown().length()).max().orElse(0) + AROUND_ITEM;
  }

  @Override
  public void accept(MenuVisitor visitor) {
    visitor.visitMenu(this);
    items.forEach(visitor::visitMenuItem);
  }
}
