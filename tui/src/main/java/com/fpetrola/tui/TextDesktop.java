package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    opened.forEach(window -> window.accept(new Painter(screen, focused)));
  }

  private Optional<Window> top() {
    return opened.stream().reduce((below, above) -> above);
  }

  /** What each key does to the desktop: Tab moves the focus, Enter presses the focused button. */
  private class Typed implements KeyVisitor {
    @Override
    public void enter() {
      top().ifPresent(window -> window.accept(new PressFocused(focused)));
    }

    /** Moves the focus to the next button, back to the first after the last. */
    @Override
    public void tab() {
      List<Button> buttons = new ArrayList<>();
      top().ifPresent(window -> window.accept(new CollectButtons(buttons)));
      focused = (focused + 1) % Math.max(1, buttons.size());
    }

    @Override
    public void other() {
    }
  }
}
