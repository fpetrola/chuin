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
    return new TextWindow();
  }

  @Override
  public Button button(String caption) {
    return new TextButton();
  }

  @Override
  public Keys keys() {
    return keys;
  }
}
