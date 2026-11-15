package com.fpetrola.chuin;

import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.input.KeyVisitor;


/** A key that goes on to the windows, whatever key it is; subclasses keep the ones they handle themselves. */
class ToWindows implements KeyVisitor {
  private final Key key;
  private final Windows windows;

  ToWindows(Key key, Windows windows) {
    this.key = key;
    this.windows = windows;
  }

  /** Hands the key to the windows. */
  private void forward() {
    windows.type(key);
  }

  @Override
  public void enter() {
    forward();
  }

  @Override
  public void tab() {
    forward();
  }

  @Override
  public void space() {
    forward();
  }

  @Override
  public void character(char typed) {
    forward();
  }

  @Override
  public void backspace() {
    forward();
  }

  @Override
  public void up() {
    forward();
  }

  @Override
  public void down() {
    forward();
  }

  @Override
  public void left() {
    forward();
  }

  @Override
  public void right() {
    forward();
  }

  @Override
  public void control(char letter) {
    forward();
  }

  @Override
  public void other() {
    forward();
  }

  @Override
  public void escape() {
    forward();
  }

  @Override
  public void function(int number) {
    forward();
  }

  @Override
  public void alt(char letter) {
    forward();
  }
}
