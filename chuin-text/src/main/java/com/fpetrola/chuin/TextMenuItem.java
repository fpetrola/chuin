package com.fpetrola.chuin;

import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuItem;

import java.util.Optional;

/** An item of a menu, with the menu it belongs to and the command it runs. */
class TextMenuItem implements MenuItem {
  private final Menu menu;
  private final String shown;
  private final Optional<Key> key;
  private final Runnable command;

  TextMenuItem(Menu menu, String shown, Optional<Key> key, Runnable command) {
    this.menu = menu;
    this.shown = shown;
    this.key = key;
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
  public Optional<Key> key() {
    return key;
  }

  @Override
  public void run() {
    command.run();
  }
}
