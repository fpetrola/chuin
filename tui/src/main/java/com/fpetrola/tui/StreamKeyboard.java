package com.fpetrola.tui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** Reads bytes from a stream until it ends and types each as a key. */
class StreamKeyboard implements Keyboard {
  private static final int END = -1;
  private static final int CARRIAGE_RETURN = '\r';
  private final InputStream in;
  private final Key enter;

  StreamKeyboard(InputStream in, Key enter) {
    this.in = in;
    this.enter = enter;
  }

  @Override
  public void typeInto(Desktop desktop) {
    try {
      for (int read = in.read(); read != END; read = in.read()) {
        desktop.type(read == CARRIAGE_RETURN ? enter : new TextKey());
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
