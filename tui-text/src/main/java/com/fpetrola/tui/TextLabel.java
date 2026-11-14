package com.fpetrola.tui;

class TextLabel implements Label {
  private String text;

  TextLabel(String text) {
    this.text = text;
  }

  @Override
  public void text(String text) {
    this.text = text;
  }

  @Override
  public String text() {
    return text;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitLabel(this);
  }
}
