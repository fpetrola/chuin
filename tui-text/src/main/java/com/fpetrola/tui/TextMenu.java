package com.fpetrola.tui;

class TextMenu implements Menu {
  private final String title;

  TextMenu(String title) {
    this.title = title;
  }

  @Override
  public String title() {
    return title;
  }
}
