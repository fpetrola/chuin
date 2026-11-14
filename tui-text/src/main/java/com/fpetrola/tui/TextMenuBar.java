package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextMenuBar implements MenuBar {
  private final List<Menu> menus = new ArrayList<>();

  @Override
  public void add(Menu menu) {
    menus.add(menu);
  }

  @Override
  public void accept(MenuVisitor visitor) {
    visitor.visitMenuBar(this);
    menus.forEach(menu -> menu.accept(visitor));
  }
}
