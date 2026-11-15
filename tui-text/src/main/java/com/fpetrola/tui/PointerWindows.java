package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.input.Key;
import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.view.Window;

import java.util.function.Consumer;

/**
 * Windows that take a drag and its release only after a click went to them: a drag belongs to the click that started
 * it, so one that started on the menu bar or the status line, which never reached the windows, moves nothing.
 */
class PointerWindows implements Windows {
  private static final Consumer<Point> NOWHERE = cell -> { };
  private final Windows windows;
  private Consumer<Point> dragged = NOWHERE;
  private Consumer<Point> released = NOWHERE;

  PointerWindows(Windows windows) {
    this.windows = windows;
  }

  @Override
  public void open(Window window) {
    windows.open(window);
  }

  @Override
  public void type(Key key) {
    windows.type(key);
  }

  @Override
  public void click(Point cell) {
    windows.click(cell);
    dragged = windows::drag;
    released = this::letGo;
  }

  @Override
  public void drag(Point cell) {
    dragged.accept(cell);
  }

  @Override
  public void release(Point cell) {
    released.accept(cell);
  }

  @Override
  public void draw(Screen screen) {
    windows.draw(screen);
  }

  @Override
  public void escape(Runnable otherwise) {
    windows.escape(otherwise);
  }

  /** Ends the drag the last click started: the windows take the release, and later moves go nowhere. */
  private void letGo(Point cell) {
    windows.release(cell);
    dragged = NOWHERE;
    released = NOWHERE;
  }
}
