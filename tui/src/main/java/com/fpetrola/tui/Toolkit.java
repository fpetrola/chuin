package com.fpetrola.tui;

public interface Toolkit {
  Desktop desktop();

  Window window(String title);

  Button button(String caption);

  Keys keys();
}
