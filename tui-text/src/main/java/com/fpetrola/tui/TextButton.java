package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextButton implements Button {
  /** The two blanks on each side of the caption. */
  private static final int FACE = 4;
  private final String caption;
  private final List<ButtonListener> listeners = new ArrayList<>();
  /** Whether the mouse holds it down. */
  private boolean held;

  TextButton(String caption) {
    this.caption = caption;
  }

  @Override
  public void onPress(ButtonListener listener) {
    listeners.add(listener);
  }

  @Override
  public String caption() {
    return caption;
  }

  @Override
  public void press() {
    List.copyOf(listeners).forEach(listener -> listener.pressed(this));
  }

  @Override
  public void hold() {
    held = true;
  }

  @Override
  public void letGo() {
    held = false;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitButton(this);
    if (held) {
      visitor.visitHeldButton(this);
    }
  }

  /** Its face; its shadow falls on what is around it, as a window's does, and takes no room. */
  @Override
  public Size size() {
    return new TextSize(new TextCaption(caption).shown().length() + FACE, 1);
  }
}
