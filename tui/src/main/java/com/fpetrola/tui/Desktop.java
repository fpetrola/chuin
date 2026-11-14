package com.fpetrola.tui;

public interface Desktop {
  void open(Window window);

  void type(Key key);

  void draw(Screen screen);
}
