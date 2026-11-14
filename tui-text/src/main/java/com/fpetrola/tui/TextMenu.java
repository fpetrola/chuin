package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

class TextMenu implements Menu {
  /** A blank on each side of an item and the two borders of the box. */
  private static final int AROUND_ITEM = 4;
  /** The blanks between an item and the name of its key. */
  private static final int BEFORE_KEY = 2;
  private final String title;
  private final List<MenuItem> items = new ArrayList<>();
  /** The items and separators in their order, each ready to meet a visitor. */
  private final List<Consumer<MenuVisitor>> rows = new ArrayList<>();

  TextMenu(String title) {
    this.title = title;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public void add(String item, Runnable command) {
    add(new TextMenuItem(this, item, Optional.empty(), command));
  }

  @Override
  public void add(String item, Key key, Runnable command) {
    add(new TextMenuItem(this, item, Optional.of(key), command));
  }

  private void add(MenuItem item) {
    items.add(item);
    rows.add(visitor -> visitor.visitMenuItem(item));
  }

  @Override
  public void separator() {
    rows.add(visitor -> visitor.visitMenuSeparator(this));
  }

  @Override
  public int width() {
    int text = items.stream().mapToInt(item -> new TextCaption(item.text()).shown().length()).max().orElse(0);
    int key = items.stream().mapToInt(item -> item.key().map(TextMenu::label).orElse("").length()).max().orElse(0);
    return text + (key == 0 ? 0 : BEFORE_KEY + key) + AROUND_ITEM;
  }

  @Override
  public void accept(MenuVisitor visitor) {
    visitor.visitMenu(this);
    rows.forEach(row -> row.accept(visitor));
  }

  private static String label(Key key) {
    StringBuilder label = new StringBuilder();
    key.accept(new KeyLabel(label));
    return label.toString();
  }
}
