package com.fpetrola.tui;

import java.util.function.Consumer;

/** A frame that stands over what is under it: the frame itself and the shadow it casts. Windows and open menus are drawn this way. */
class RaisedFrame implements Consumer<Screen> {
  private final Consumer<Screen> drawn;

  RaisedFrame(Area area, String title, char tint) {
    drawn = new Frame(area, title, tint).andThen(new Shadow(area));
  }

  @Override
  public void accept(Screen screen) {
    drawn.accept(screen);
  }
}
