package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextButton implements Button {
  private final List<ButtonListener> listeners = new ArrayList<>();

  @Override
  public void onPress(ButtonListener listener) {
    listeners.add(listener);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.button(this, List.copyOf(listeners));
  }
}
