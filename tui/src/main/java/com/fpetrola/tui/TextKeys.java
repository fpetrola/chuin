package com.fpetrola.tui;

class TextKeys implements Keys {
  private final Key enter = new TextKey();

  @Override
  public Key enter() {
    return enter;
  }
}
