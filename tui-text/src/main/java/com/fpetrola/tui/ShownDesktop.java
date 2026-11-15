package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.input.Key;
import com.fpetrola.tui.menu.MenuBar;
import com.fpetrola.tui.menu.StatusLine;
import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.screen.Terminal;
import com.fpetrola.tui.view.Window;


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
