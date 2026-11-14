package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A desktop with a menu bar over the windows of another. While no menu is open, keys and clicks go to the windows,
 * except F10 and clicks on the bar. While a menu is open, keys and clicks stay with the menus: the arrows walk items
 * and menus, Enter or a click on an item runs it, and F10 or a click away closes it. Each of the two is a state.
 */
class MenuDesktop implements Desktop {
  private static final int MENU_KEY = 10;
  private static final int NONE = -1;
  private final Windows windows;
  private MenuBar bar = new TextMenuBar();
  private Desktop state = new Closed();
  /** The command of each bound key, by the key's name. */
  private final Map<String, Runnable> bindings = new HashMap<>();

  MenuDesktop(Windows windows) {
    this.windows = windows;
  }

  @Override
  public void open(Window window) {
    windows.open(window);
  }

  @Override
  public void show(MenuBar shown) {
    bar = shown;
  }

  @Override
  public void bind(Key key, Runnable command) {
    bindings.put(name(key), command);
  }

  /** A bound key runs its command and closes any open menu; any other key goes to the state. */
  @Override
  public void type(Key key) {
    Runnable bound = bindings.get(name(key));
    if (bound == null) {
      state.type(key);
    } else {
      state = new Closed();
      bound.run();
    }
  }

  private static String name(Key key) {
    StringBuilder name = new StringBuilder();
    key.accept(new KeyName(name));
    return name.toString();
  }

  @Override
  public void click(Point cell) {
    state.click(cell);
  }

  @Override
  public void draw(Screen screen) {
    windows.draw(screen);
    state.draw(screen);
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

  /** Where each menu's title starts on the bar, left to right, and where the last one ends. */
  private List<Integer> titleStarts() {
    List<Integer> starts = new ArrayList<>(List.of(0));
    menus().forEach(menu -> starts.add(starts.getLast() + new TextCaption(menu.title()).shown().length() + 2));
    return starts;
  }

  /** Which menu's title covers this cell, or NONE when it is not on a title. */
  private int titleAt(Point cell) {
    List<Integer> starts = titleStarts();
    for (int at = 0; at + 1 < starts.size(); at++) {
      if (cell.row() == 0 && cell.column() >= starts.get(at) && cell.column() < starts.get(at + 1)) {
        return at;
      }
    }
    return NONE;
  }

  /** Which menu's title answers to this letter, or NONE when none does. */
  private int menuAnswering(char letter) {
    List<Menu> menus = menus();
    for (int at = 0; at < menus.size(); at++) {
      if (new TextCaption(menus.get(at).title()).answers(letter)) {
        return at;
      }
    }
    return NONE;
  }

  /** The state every desktop call lands in; open and show are the menu desktop's, whatever the state. */
  private abstract class MenuState implements Desktop {
    @Override
    public void bind(Key key, Runnable command) {
      MenuDesktop.this.bind(key, command);
    }

    @Override
    public void open(Window window) {
      MenuDesktop.this.open(window);
    }

    @Override
    public void show(MenuBar shown) {
      MenuDesktop.this.show(shown);
    }
  }

  /** No menu is open: everything goes to the windows, but F10 and clicks on the bar, which open a menu. */
  private class Closed extends MenuState {
    @Override
    public void type(Key key) {
      key.accept(new ClosedKeys(key));
    }

    @Override
    public void click(Point cell) {
      int title = titleAt(cell);
      boolean onBar = cell.row() == 0 && !menus().isEmpty();
      if (title != NONE) {
        state = new Opened(title);
      } else if (!onBar) {
        windows.click(cell);
      }
    }

    @Override
    public void draw(Screen screen) {
      if (!menus().isEmpty()) {
        bar.accept(new MenuPainter(screen, Optional.empty(), 0));
      }
    }
  }

  /** A menu is open, with one of its items selected: keys and clicks stay with the menus. */
  private class Opened extends MenuState {
    private final int open;
    private int item;

    Opened(int open) {
      this.open = open;
    }

    @Override
    public void type(Key key) {
      key.accept(new OpenedKeys(this));
    }

    @Override
    public void click(Point cell) {
      int title = titleAt(cell);
      int row = cell.row() - 2;
      int start = titleStarts().get(open);
      boolean across = cell.column() >= start && cell.column() < start + menus().get(open).width();
      List<MenuItem> items = items();
      state = title == NONE ? new Closed() : new Opened(title);
      if (title == NONE && across && row >= 0 && row < items.size()) {
        items.get(row).run();
      }
    }

    @Override
    public void draw(Screen screen) {
      screen.hideCursor();
      bar.accept(new MenuPainter(screen, Optional.of(menus().get(open)), item));
    }

    /** The items of this menu, top to bottom. */
    private List<MenuItem> items() {
      Menu shown = menus().get(open);
      List<MenuItem> items = new ArrayList<>();
      bar.accept(new MenuVisitor() {
        @Override
        public void visitMenuItem(MenuItem each) {
          if (each.menu() == shown) {
            items.add(each);
          }
        }
      });
      return items;
    }
  }

  /** Keys while no menu is open: F10 opens the first one when there is a bar, any other key goes to the windows. */
  private class ClosedKeys implements KeyVisitor {
    private final Key key;

    ClosedKeys(Key key) {
      this.key = key;
    }

    @Override
    public void enter() {
      windows.type(key);
    }

    @Override
    public void tab() {
      windows.type(key);
    }

    @Override
    public void space() {
      windows.type(key);
    }

    @Override
    public void character(char typed) {
      windows.type(key);
    }

    @Override
    public void backspace() {
      windows.type(key);
    }

    @Override
    public void up() {
      windows.type(key);
    }

    @Override
    public void down() {
      windows.type(key);
    }

    @Override
    public void left() {
      windows.type(key);
    }

    @Override
    public void right() {
      windows.type(key);
    }

    @Override
    public void control(char letter) {
      windows.type(key);
    }

    @Override
    public void function(int number) {
      if (number == MENU_KEY && !menus().isEmpty()) {
        state = new Opened(0);
      } else {
        windows.type(key);
      }
    }

    @Override
    public void other() {
      windows.type(key);
    }

    @Override
    public void escape() {
      windows.type(key);
    }

    /** A menu whose title answers opens; otherwise the windows get it. */
    @Override
    public void alt(char letter) {
      int menu = menuAnswering(letter);
      if (menu == NONE) {
        windows.type(key);
      } else {
        state = new Opened(menu);
      }
    }
  }

  /** Keys while a menu is open: they stay with the menus. */
  private class OpenedKeys implements KeyVisitor {
    private final Opened opened;

    OpenedKeys(Opened opened) {
      this.opened = opened;
    }

    @Override
    public void enter() {
      List<MenuItem> items = opened.items();
      state = new Closed();
      if (opened.item < items.size()) {
        items.get(opened.item).run();
      }
    }

    @Override
    public void up() {
      opened.item = Math.max(0, opened.item - 1);
    }

    @Override
    public void down() {
      opened.item = Math.max(0, Math.min(opened.items().size() - 1, opened.item + 1));
    }

    @Override
    public void left() {
      int count = menus().size();
      state = new Opened((opened.open + count - 1) % count);
    }

    @Override
    public void right() {
      state = new Opened((opened.open + 1) % menus().size());
    }

    @Override
    public void function(int number) {
      if (number == MENU_KEY) {
        state = new Closed();
      }
    }

    @Override
    public void escape() {
      state = new Closed();
    }

    /** The item of the open menu whose caption answers runs, and the menu closes. */
    @Override
    public void character(char typed) {
      for (MenuItem each : opened.items()) {
        if (new TextCaption(each.text()).answers(typed)) {
          state = new Closed();
          each.run();
          return;
        }
      }
    }

    /** Another menu whose title answers opens instead. */
    @Override
    public void alt(char letter) {
      int menu = menuAnswering(letter);
      if (menu != NONE) {
        state = new Opened(menu);
      }
    }
  }
}
