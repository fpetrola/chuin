package com.fpetrola.tui;

public interface Button {
  void onPress(ButtonListener listener);

  void accept(ViewVisitor visitor);
}
