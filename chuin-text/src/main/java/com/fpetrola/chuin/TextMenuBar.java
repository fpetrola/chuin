package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.MenuVisitor;

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
