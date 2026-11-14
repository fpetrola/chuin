package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class TextMenu implements Menu {
  private final String title;
  /** Each item already paired with its command, top to bottom. */
  private final List<Consumer<MenuItemVisitor>> items = new ArrayList<>();

  TextMenu(String title) {
    this.title = title;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public void add(String item, Runnable command) {
    items.add(visitor -> visitor.item(item, command));
  }

  @Override
  public void acceptItems(MenuItemVisitor visitor) {
    items.forEach(each -> each.accept(visitor));
  }
}
