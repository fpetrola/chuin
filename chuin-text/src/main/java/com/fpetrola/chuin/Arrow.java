package com.fpetrola.chuin;

import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.input.KeyVisitor;


/** The arrow keys, in the order of the bytes that end their escape sequences: ESC [ A, B, C, D. */
enum Arrow implements Key {
  UP(KeyVisitor::up),
  DOWN(KeyVisitor::down),
  RIGHT(KeyVisitor::right),
  LEFT(KeyVisitor::left);

  private final Key key;

  Arrow(Key key) {
    this.key = key;
  }

  @Override
  public void accept(KeyVisitor visitor) {
    key.accept(visitor);
  }
}
