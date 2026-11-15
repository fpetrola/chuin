package com.fpetrola.chuin.input;

public interface Key {
  void accept(KeyVisitor visitor);
}
