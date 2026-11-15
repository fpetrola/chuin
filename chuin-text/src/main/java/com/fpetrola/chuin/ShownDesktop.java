package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.screen.Terminal;
import com.fpetrola.chuin.view.Window;


/** A desktop that is drawn and shown on a terminal every time it is drawn or typed into. */
class ShownDesktop implements Desktop {
  private final Desktop desktop;
  private final Terminal terminal;
  private final Screen screen;

  ShownDesktop(Desktop desktop, Terminal terminal, Screen screen) {
    this.desktop = desktop;
    this.terminal = terminal;
    this.screen = screen;
  }

  @Override
  public void open(Window window) {
    desktop.open(window);
  }

  @Override
  public void bind(Key key, Runnable command) {
    desktop.bind(key, command);
  }

  @Override
  public void type(Key key) {
    desktop.type(key);
    draw(screen);
  }

  @Override
  public void drag(Point cell) {
    desktop.drag(cell);
    draw(screen);
  }

  @Override
  public void release(Point cell) {
    desktop.release(cell);
    draw(screen);
  }

  @Override
  public void click(Point cell) {
    desktop.click(cell);
    draw(screen);
  }

  @Override
  public void show(StatusLine status) {
    desktop.show(status);
  }

  @Override
  public void show(MenuBar bar) {
    desktop.show(bar);
  }

  @Override
  public void draw(Screen target) {
    desktop.draw(target);
    terminal.show(target);
  }
}
