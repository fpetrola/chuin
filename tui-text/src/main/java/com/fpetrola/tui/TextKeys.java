package com.fpetrola.tui;

class TextKeys implements Keys {
  private final Key enter = KeyVisitor::enter;
  private final Key tab = KeyVisitor::tab;
  private final Key space = KeyVisitor::space;

  @Override
  public Key space() {
    return space;
  }

  @Override
  public Key enter() {
    return enter;
  }

  @Override
  public Key tab() {
    return tab;
  }
}
