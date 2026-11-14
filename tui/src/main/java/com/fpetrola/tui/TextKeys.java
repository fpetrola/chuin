package com.fpetrola.tui;

class TextKeys implements Keys {
  private final Key enter = KeyVisitor::enter;
  private final Key tab = KeyVisitor::tab;

  @Override
  public Key enter() {
    return enter;
  }

  @Override
  public Key tab() {
    return tab;
  }
}
