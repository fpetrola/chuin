package com.fpetrola.tui;

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
  public void type(Key key) {
    desktop.type(key);
    draw(screen);
  }

  @Override
  public void click(Point cell) {
    desktop.click(cell);
    draw(screen);
  }

  @Override
  public void draw(Screen target) {
    desktop.draw(target);
    terminal.show(target);
  }
}
