package com.fpetrola.chuin;

import com.fpetrola.chuin.input.KeyVisitor;


/** Names a key by what it is, so two keys are the same key when their names are equal, however they were made. */
class KeyName implements KeyVisitor {
  private final StringBuilder name;

  KeyName(StringBuilder name) {
    this.name = name;
  }

  @Override
  public void enter() {
    name.append("enter");
  }

  @Override
  public void tab() {
    name.append("tab");
  }

  @Override
  public void space() {
    name.append("space");
  }

  @Override
  public void character(char typed) {
    name.append("character ").append(typed);
  }

  @Override
  public void backspace() {
    name.append("backspace");
  }

  @Override
  public void up() {
    name.append("up");
  }

  @Override
  public void down() {
    name.append("down");
  }

  @Override
  public void left() {
    name.append("left");
  }

  @Override
  public void right() {
    name.append("right");
  }

  @Override
  public void control(char letter) {
    name.append("control ").append(letter);
  }

  @Override
  public void function(int number) {
    name.append("function ").append(number);
  }

  @Override
  public void escape() {
    name.append("escape");
  }

  @Override
  public void alt(char letter) {
    name.append("alt ").append(letter);
  }

  @Override
  public void other() {
    name.append("other");
  }
}
