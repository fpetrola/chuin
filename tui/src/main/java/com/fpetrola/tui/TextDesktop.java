package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextDesktop implements Desktop {
  private final Key enter;
  private final List<Window> opened = new ArrayList<>();

  TextDesktop(Key enter) {
    this.enter = enter;
  }

  @Override
  public void open(Window window) {
    opened.add(window);
  }

  @Override
  public void type(Key key) {
    opened.stream().reduce((below, above) -> above).ifPresent(top -> top.accept(new KeyDispatch(key, enter)));
  }

  @Override
  public void draw(Screen screen) {
    opened.forEach(window -> window.accept(new Painter(screen)));
  }
}
