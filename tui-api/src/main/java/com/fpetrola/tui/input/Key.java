package com.fpetrola.tui.input;

public interface Key {
  void accept(KeyVisitor visitor);
}
