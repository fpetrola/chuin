package com.fpetrola.tui;

/** Writes the name a key is shown by, as in a menu or a status line: "F3", "Esc", "Alt+X", "Ctrl+W". */
class KeyLabel implements KeyVisitor {
  private final StringBuilder label;

  KeyLabel(StringBuilder label) {
    this.label = label;
  }

  @Override
  public void enter() {
    label.append("Enter");
  }

  @Override
  public void tab() {
    label.append("Tab");
  }

  @Override
  public void space() {
    label.append("Space");
  }

  @Override
  public void character(char typed) {
    label.append(typed);
  }

  @Override
  public void backspace() {
    label.append("Backspace");
  }

  @Override
  public void control(char letter) {
    label.append("Ctrl+").append(Character.toUpperCase(letter));
  }

  @Override
  public void function(int number) {
    label.append('F').append(number);
  }

  @Override
  public void escape() {
    label.append("Esc");
  }

  @Override
  public void alt(char letter) {
    label.append("Alt+").append(Character.toUpperCase(letter));
  }
}
