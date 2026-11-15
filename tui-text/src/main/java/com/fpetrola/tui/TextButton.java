package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextButton implements Button {
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
}
