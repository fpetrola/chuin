package com.fpetrola.tui;

import java.io.InputStream;
import java.io.OutputStream;

public interface Toolkit {
  Desktop desktop();

  Window window(String title);

  Button button(String caption);

  Label label(String text);

  Keys keys();

  Screen screen(int columns, int rows);

  Area area(int column, int row, int columns, int rows);

  Point point(int column, int row);

  Terminal terminal(OutputStream out);

  Keyboard keyboard(InputStream in);

  Console console(InputStream in, OutputStream out, Screen screen);
}
