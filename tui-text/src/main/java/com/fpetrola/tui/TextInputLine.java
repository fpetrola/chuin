package com.fpetrola.tui;

class TextInputLine implements InputLine {
  private final StringBuilder text = new StringBuilder();
  private final int width;

  TextInputLine(int width) {
    this.width = width;
  }

  @Override
  public String text() {
    return text.toString();
  }

  @Override
  public int width() {
    return width;
  }

  @Override
  public void type(char typed) {
    text.append(typed);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitInputLine(this);
  }

  @Override
  public void erase() {
    if (!text.isEmpty()) {
      text.setLength(text.length() - 1);
    }
  }
}
