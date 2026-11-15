package com.fpetrola.tui;

import com.fpetrola.tui.input.Key;
import com.fpetrola.tui.screen.Screen;


/** The name a key is shown by, "F3" or "Esc", as a caption with no marked letter: it highlights nothing and answers to no letter. */
class KeyCaption implements Caption {
  private final String shown;

  KeyCaption(Key key) {
    StringBuilder name = new StringBuilder();
    key.accept(new KeyLabel(name));
    this.shown = name.toString();
  }

  @Override
  public String shown() {
    return shown;
  }

  @Override
  public void highlight(Screen screen, int column, int row, char tint) {
  }

  @Override
  public boolean answers(char letter) {
    return false;
  }
}
