package com.fpetrola.tui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** Reads bytes from a stream until it ends or Ctrl+Q is typed, and types each as a key. */
class StreamKeyboard implements Keyboard {
  private static final int END = -1;
  private static final int CONTROL_Q = 0x11;
  private static final int CARRIAGE_RETURN = '\r';
  private static final int TAB = '\t';
  private static final int BACKSPACE = 0x7F;
  private static final int SPACE = ' ';
  private static final int FIRST_PRINTABLE = '!';
  private static final int LAST_PRINTABLE = '~';
  private final InputStream in;
  private final Keys keys;

  StreamKeyboard(InputStream in, Keys keys) {
    this.in = in;
    this.keys = keys;
  }

  @Override
  public void typeInto(Desktop desktop) {
    try {
      for (int read = in.read(); read != END && read != CONTROL_Q; read = in.read()) {
        desktop.type(keyFor(read));
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Key keyFor(int read) {
    boolean printable = read >= FIRST_PRINTABLE && read <= LAST_PRINTABLE;
    Key written = printable ? visitor -> visitor.character((char) read) : KeyVisitor::other;
    Key erased = read == BACKSPACE ? KeyVisitor::backspace : written;
    Key spaced = read == SPACE ? keys.space() : erased;
    Key named = read == TAB ? keys.tab() : spaced;
    return read == CARRIAGE_RETURN ? keys.enter() : named;
  }
}
