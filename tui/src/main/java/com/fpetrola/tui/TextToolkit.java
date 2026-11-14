package com.fpetrola.tui;

public class TextToolkit implements Toolkit {
  private final TextDesktop desktop = new TextDesktop();
  private final TextKeys keys = new TextKeys();

  public TextToolkit() {
  }

  @Override
  public Desktop desktop() {
    return desktop;
  }

  @Override
  public Window window(String title) {
    return new TextWindow(title);
  }

  @Override
  public Button button(String caption) {
    return new TextButton(caption);
  }

  @Override
  public Keys keys() {
    return keys;
  }

  @Override
  public Screen screen(int columns, int rows) {
    return new TextScreen(columns, rows);
  }

  @Override
  public Area area(int column, int row, int columns, int rows) {
    return new TextArea(column, row, columns, rows);
  }

  @Override
  public Point point(int column, int row) {
    return new TextPoint(column, row);
  }
}
