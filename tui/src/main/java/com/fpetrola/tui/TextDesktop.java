package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextDesktop implements Desktop {
  private final List<Window> opened = new ArrayList<>();
  private int focused;

  @Override
  public void open(Window window) {
    opened.add(window);
    focused = 0;
  }

  @Override
  public void type(Key key) {
    key.accept(new Typed());
  }

  @Override
  public void draw(Screen screen) {
    opened.forEach(window -> window.accept(new Painter(screen)));
  }

  /** What each key does to the desktop: Tab moves the focus, Enter presses the focused button. */
  private class Typed implements KeyVisitor {
    @Override
    public void enter() {
      opened.stream().reduce((below, above) -> above).ifPresent(top -> top.accept(new PressFocused(focused)));
    }

    @Override
    public void tab() {
      focused++;
    }

    @Override
    public void other() {
    }
  }
}
