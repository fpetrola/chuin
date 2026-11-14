package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

/**
 * A desktop with a menu bar over the windows of another. F10 opens the first menu; while a menu is open, every key
 * goes to it and none to the windows: the arrows walk its items and the menus, wrapping at the ends, Enter runs the
 * selected item and closes it, and F10 closes it.
 */
class MenuDesktop implements Desktop {
  private static final int MENU_KEY = 10;
  private static final int CLOSED = -1;
  private final Desktop windows;
  private MenuBar bar = new TextMenuBar();
  /** Which menu is open, or CLOSED. */
  private int open = CLOSED;
  private int item;

  MenuDesktop(Desktop windows) {
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
  public void type(Key key) {
    if (open == CLOSED) {
      key.accept(new Closed(key));
    } else {
      key.accept(new Opened());
    }
  }

  @Override
  public void click(Point cell) {
    windows.click(cell);
  }

  @Override
  public void draw(Screen screen) {
    windows.draw(screen);
    List<Menu> menus = menus();
    if (!menus.isEmpty()) {
      new MenuPainter(menus, open, item).accept(screen);
    }
  }

  private List<Menu> menus() {
    List<Menu> menus = new ArrayList<>();
    bar.acceptMenus(menus::add);
    return menus;
  }

  private List<Runnable> commands() {
    List<Runnable> commands = new ArrayList<>();
    menus().get(open).acceptItems((text, command) -> commands.add(command));
    return commands;
  }

  /** Keys while no menu is open: F10 opens the first one when there is a bar, any other key goes to the windows. */
  private class Closed implements KeyVisitor {
    private final Key key;

    Closed(Key key) {
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
        open = 0;
        item = 0;
      } else {
        windows.type(key);
      }
    }

    @Override
    public void other() {
      windows.type(key);
    }
  }

  /** Keys while a menu is open: they stay with the menus. */
  private class Opened implements KeyVisitor {
    @Override
    public void enter() {
      List<Runnable> commands = commands();
      int chosen = item;
      open = CLOSED;
      if (chosen < commands.size()) {
        commands.get(chosen).run();
      }
    }

    @Override
    public void up() {
      item = Math.max(0, item - 1);
    }

    @Override
    public void down() {
      item = Math.max(0, Math.min(commands().size() - 1, item + 1));
    }

    @Override
    public void left() {
      int count = menus().size();
      open = (open + count - 1) % count;
      item = 0;
    }

    @Override
    public void right() {
      open = (open + 1) % menus().size();
      item = 0;
    }

    @Override
    public void function(int number) {
      if (number == MENU_KEY) {
        open = CLOSED;
      }
    }
  }
}
