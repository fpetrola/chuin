package com.fpetrola.chuin.text.menu;

import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.StatusItem;


/** An entry of the status line: its marked text, its key and its command. */
class TextStatusItem implements StatusItem {
  private final String label;
  private final Key key;
  private final Runnable command;

  TextStatusItem(String label, Key key, Runnable command) {
    this.label = label;
    this.key = key;
    this.command = command;
  }

  @Override
  public String text() {
    return label;
  }

  @Override
  public Key key() {
    return key;
  }

  @Override
  public void run() {
    command.run();
  }
}
