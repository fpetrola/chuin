package com.fpetrola.tui;

class TextInputLine implements InputLine {
  private final StringBuilder text = new StringBuilder();
  private final int width;

  TextInputLine(int width) {
    this.width = width;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.inputLine(this, text.toString(), width, text::append);
  }
}
