package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/** The titles of a menu bar laid out on the top row, each as wide as its caption and a space on both sides. */
class BarTitles implements MenuTitles {
  private final MenuBar bar;

  BarTitles(MenuBar bar) {
    this.bar = bar;
  }

  @Override
  public int count() {
    return menus().size();
  }

  @Override
  public Menu menu(int at) {
    return menus().get(at);
  }

  @Override
  public int start(int at) {
    return starts().get(at);
  }

  @Override
  public OptionalInt titleAt(Point cell) {
    List<Integer> starts = starts();
    for (int at = 0; at + 1 < starts.size(); at++) {
      if (cell.row() == 0 && cell.column() >= starts.get(at) && cell.column() < starts.get(at + 1)) {
        return OptionalInt.of(at);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public OptionalInt answering(char letter) {
    List<Menu> menus = menus();
    for (int at = 0; at < menus.size(); at++) {
      if (new TextCaption(menus.get(at).title()).answers(letter)) {
        return OptionalInt.of(at);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public List<Optional<MenuItem>> rows(int at) {
    List<Optional<MenuItem>> rows = new ArrayList<>();
    menu(at).accept(new MenuVisitor() {
      @Override
      public void visitMenuItem(MenuItem each) {
        rows.add(Optional.of(each));
      }

      @Override
      public void visitMenuSeparator(Menu menu) {
        rows.add(Optional.empty());
      }
    });
    return rows;
  }

  @Override
  public List<MenuItem> items(int at) {
    List<MenuItem> items = new ArrayList<>();
    rows(at).forEach(row -> row.ifPresent(items::add));
    return items;
  }

  private List<Menu> menus() {
    List<Menu> menus = new ArrayList<>();
    bar.accept(new MenuVisitor() {
      @Override
      public void visitMenu(Menu menu) {
        menus.add(menu);
      }
    });
    return menus;
  }

  /** Where each title starts, left to right, and where the last one ends. */
  private List<Integer> starts() {
    List<Integer> starts = new ArrayList<>(List.of(0));
    menus().forEach(menu -> starts.add(starts.getLast() + new TextCaption(menu.title()).shown().length() + 2));
    return starts;
  }
}
