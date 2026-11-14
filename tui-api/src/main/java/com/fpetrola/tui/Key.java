package com.fpetrola.tui;

public interface Key {
  void accept(KeyVisitor visitor);
}
