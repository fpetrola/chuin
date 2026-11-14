package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

class TextDesktop implements Desktop {
  private final List<Window> opened = new ArrayList<>();
  /** The tint of a cell no window covers. */
  private static final char BARE = '.';
  /** A focus no view has: windows below the top one are drawn with it. */
  private static final int UNFOCUSED = -1;
  /** F6 raises the bottom window over the others, as in Turbo Vision. */
  private static final int NEXT_WINDOW = 6;
  private int focused;
  /** The focus each window had when it last left the top, by its place in {@link #opened}. */
  private final List<Integer> remembered = new ArrayList<>();

  @Override
  public void open(Window window) {
    if (!opened.isEmpty()) {
      remembered.set(opened.size() - 1, focused);
    }
    opened.add(window);
    remembered.add(0);
    focused = 0;
  }

  @Override
  public void type(Key key) {
    key.accept(new Typed());
  }

  @Override
  public void draw(Screen screen) {
    clear(screen);
    int last = opened.size() - 1;
    for (int at = 0; at < opened.size(); at++) {
      opened.get(at).accept(new Painter(screen, at == last ? focused : UNFOCUSED));
    }
  }

  /** Hands ↑ or ↓ to the focused view; when there is none, the window moves. */
  private void arrow(BinaryOperator<Runnable> choose, Runnable otherwise) {
    List<View> focusable = new ArrayList<>();
    top().ifPresent(window -> window.accept(new CollectFocusable(focusable)));
    if (focused < focusable.size()) {
      focusable.get(focused).accept(new ListArrow(choose, otherwise));
    } else {
      otherwise.run();
    }
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
      arrow((previous, next) -> previous, () -> move(0, -1));
    }

    @Override
    public void down() {
      arrow((previous, next) -> next, () -> move(0, 1));
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
    public void function(int number) {
      if (number == NEXT_WINDOW && opened.size() > 1) {
        remembered.set(opened.size() - 1, focused);
        opened.add(opened.remove(0));
        remembered.add(remembered.remove(0));
        focused = remembered.get(opened.size() - 1);
      }
    }

    @Override
    public void other() {
    }
  }
}
