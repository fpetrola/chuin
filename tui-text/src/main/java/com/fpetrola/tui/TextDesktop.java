package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TextDesktop implements Desktop {
  private final List<Window> opened = new ArrayList<>();
  /** The tint of a cell no window covers. */
  private static final char BARE = '.';
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
    clear(screen);
    opened.forEach(window -> window.accept(new Painter(screen, focused)));
  }

  /** Blanks every cell and takes its tint away, so nothing drawn before is left behind. */
  private void clear(Screen screen) {
    List<String> lines = screen.lines();
    for (int row = 0; row < lines.size(); row++) {
      int columns = lines.get(row).length();
      screen.write(0, row, " ".repeat(columns));
      screen.paint(0, row, columns, BARE);
    }
  }

  private Optional<Window> top() {
    return opened.stream().reduce((below, above) -> above);
  }

  /** Moves the top window by the given cells, wherever that leaves it: the screen cuts what falls outside. */
  private void move(int columns, int rows) {
    top().ifPresent(window -> window.accept(new MoveWindow(window, columns, rows)));
  }

  /** What each key does to the desktop: Tab moves the focus, Enter presses the focused button, Space toggles the focused check box, a letter goes into the focused input line. */
  private class Typed implements KeyVisitor {
    @Override
    public void enter() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new PressFocused())));
    }

    /** Moves the focus to the next focusable view, back to the first after the last. */
    @Override
    public void tab() {
      List<View> focusable = new ArrayList<>();
      top().ifPresent(window -> window.accept(new CollectFocusable(focusable)));
      focused = (focused + 1) % Math.max(1, focusable.size());
    }

    @Override
    public void space() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new ToggleFocused())));
    }

    @Override
    public void character(char typed) {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new TypeIntoFocused(typed))));
    }

    @Override
    public void backspace() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new EraseFocused())));
    }

    @Override
    public void up() {
      move(0, -1);
    }

    @Override
    public void down() {
      move(0, 1);
    }

    @Override
    public void left() {
      move(-1, 0);
    }

    @Override
    public void right() {
      move(1, 0);
    }

    @Override
    public void other() {
    }
  }
}
