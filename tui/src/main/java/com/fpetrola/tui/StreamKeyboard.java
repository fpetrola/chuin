package com.fpetrola.tui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** Reads bytes from a stream until it ends and types each as a key. */
class StreamKeyboard implements Keyboard {
  private static final int END = -1;
  private static final int CARRIAGE_RETURN = '\r';
  private static final int TAB = '\t';
  private final InputStream in;
  private final Keys keys;

  StreamKeyboard(InputStream in, Keys keys) {
    this.in = in;
    this.keys = keys;
  }

  @Override
  public void typeInto(Desktop desktop) {
    try {
      for (int read = in.read(); read != END; read = in.read()) {
        desktop.type(keyFor(read));
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Key keyFor(int read) {
    Key named = read == TAB ? keys.tab() : KeyVisitor::other;
    return read == CARRIAGE_RETURN ? keys.enter() : named;
  }
}
