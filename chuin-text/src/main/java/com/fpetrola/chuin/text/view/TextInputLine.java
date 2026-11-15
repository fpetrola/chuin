package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.ViewVisitor;


public class TextInputLine implements InputLine {
  private final StringBuilder text = new StringBuilder();
  private final int width;

  public TextInputLine(int width) {
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

  @Override
  public Size size() {
    return new TextSize(Math.max(width(), text().length()), 1);
  }
}
