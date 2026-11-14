package com.fpetrola.tui;

/** An item of a menu, with the menu it belongs to and the command it runs. */
class TextMenuItem implements MenuItem {
  private final Menu menu;
  private final String shown;
  private final Runnable command;

  TextMenuItem(Menu menu, String shown, Runnable command) {
    this.menu = menu;
    this.shown = shown;
    this.command = command;
  }

  @Override
  public Menu menu() {
    return menu;
  }

  @Override
  public String text() {
    return shown;
  }

  @Override
  public void run() {
    command.run();
  }
}
