package com.fpetrola.tui;

public interface Toolkit {
  Desktop desktop();

  Window window(String title);

  Button button(String caption);

  Keys keys();

  Screen screen(int columns, int rows);

  Area area(int column, int row, int columns, int rows);

  Point point(int column, int row);
}
