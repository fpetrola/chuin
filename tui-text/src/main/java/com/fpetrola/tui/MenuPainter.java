package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** Draws the menu bar across the top row and, when a menu is open, its title highlighted and its box of items under it. */
class MenuPainter implements MenuItemVisitor, Consumer<Screen> {
  private static final char BAR_TINT = 'w';
  private static final char CHOSEN_TINT = 'f';
  /** The blank and the border on each side of an item. */
  private static final int AROUND_ITEM = 4;
  private final List<Menu> menus;
  private final int open;
  private final int item;
  private final List<String> items = new ArrayList<>();

  private Screen screen;

  MenuPainter(List<Menu> menus, int open, int item) {
    this.menus = menus;
    this.open = open;
    this.item = item;
  }

  @Override
  public void item(String text, Runnable command) {
    items.add(text);
  }

  /** Paints the bar on this screen, then the open menu if there is one. */
  @Override
  public void accept(Screen target) {
    screen = target;
    int columns = screen.lines().getFirst().length();
    StringBuilder titles = new StringBuilder();
    int titleAt = 0;
    for (int at = 0; at < menus.size(); at++) {
      titleAt = at == open ? titles.length() : titleAt;
      titles.append(' ').append(menus.get(at).title()).append(' ');
    }
    screen.write(0, 0, titles + " ".repeat(Math.max(0, columns - titles.length())));
    screen.paint(0, 0, columns, BAR_TINT);
    if (open >= 0) {
      Menu menu = menus.get(open);
      screen.paint(titleAt, 0, menu.title().length() + 2, CHOSEN_TINT);
      menu.acceptItems(this);
      box(titleAt);
    }
  }

  private void box(int column) {
    if (items.isEmpty()) {
      return;
    }
    int inner = items.stream().mapToInt(String::length).max().orElse(0) + AROUND_ITEM - 2;
    screen.write(column, 1, "┌" + "─".repeat(inner) + "┐");
    for (int at = 0; at < items.size(); at++) {
      String text = items.get(at);
      screen.write(column, 2 + at, "│ " + text + " ".repeat(inner - text.length() - 1) + "│");
    }
    screen.write(column, 2 + items.size(), "└" + "─".repeat(inner) + "┘");
    for (int row = 1; row < 3 + items.size(); row++) {
      screen.paint(column, row, inner + 2, BAR_TINT);
    }
    screen.paint(column + 1, 2 + item, inner, CHOSEN_TINT);
  }
}
