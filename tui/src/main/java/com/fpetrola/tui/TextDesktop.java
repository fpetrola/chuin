package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextDesktop implements Desktop {
  private final List<Window> opened = new ArrayList<>();

  @Override
  public void open(Window window) {
    opened.add(window);
  }

  @Override
  public void type(Key key) {
    opened.stream().reduce((below, above) -> above).ifPresent(top -> top.accept(new KeyDispatch()));
  }
}
