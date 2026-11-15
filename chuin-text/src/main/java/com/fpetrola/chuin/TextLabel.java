package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.Optional;

class TextLabel implements Label {
  private String text;
  private final Optional<View> linked;

  TextLabel(String text, Optional<View> linked) {
    this.text = text;
    this.linked = linked;
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

  @Override
  public Optional<View> linked() {
    return linked;
  }

  @Override
  public Size size() {
    return new TextSize(new TextCaption(text).shown().length(), 1);
  }
}
