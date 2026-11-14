package com.fpetrola.tui;

/** Runs a desktop on a console: shows it at start and again after every key. */
class TextConsole implements Console {
  private final Keyboard keyboard;
  private final Terminal terminal;
  private final Screen screen;

  TextConsole(Keyboard keyboard, Terminal terminal, Screen screen) {
    this.keyboard = keyboard;
    this.terminal = terminal;
    this.screen = screen;
  }

  @Override
  public void run(Desktop desktop) {
    Desktop shown = new ShownDesktop(desktop, terminal, screen);
    shown.draw(screen);
    keyboard.typeInto(shown);
  }
}
