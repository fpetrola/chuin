package com.fpetrola.tui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;

/** Reads bytes from a stream until it ends or Ctrl+Q is typed, and types each as a key. */
class StreamKeyboard implements Keyboard {
  private static final int END = -1;
  private static final int CONTROL_Q = 0x11;
  private static final int CARRIAGE_RETURN = '\r';
  private static final int TAB = '\t';
  private static final int FIRST_ARROW = 'A';
  /** The ESC [ n ~ codes of F1 to F12, in order: xterm skips 16 and 22. */
  private static final int[] FUNCTION_CODES = {11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 23, 24};
  private static final int ESCAPE = 0x1B;
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
        desktop.type(read == ESCAPE ? escaped() : keyFor(read));
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Reads the rest of an escape sequence: ESC [ then digits and ';' up to a final byte. Only the arrows mean something. */
  private Key escaped() throws IOException {
    int kind = in.read();
    Key bracketed = kind == '[' ? bracketed() : KeyVisitor::other;
    return kind == 'O' ? shifted(in.read()) : bracketed;
  }

  /** ESC O P..S: how many terminals send F1 to F4. */
  private Key shifted(int last) {
    boolean function = last >= 'P' && last <= 'S';
    return function ? functionKey(last - 'P' + 1) : KeyVisitor::other;
  }

  /** ESC [ then a number, maybe more numbers after ';', and a final byte: ~ for function keys, A..D for arrows. */
  private Key bracketed() throws IOException {
    int number = 0;
    int last = in.read();
    for (; Character.isDigit(last); last = in.read()) {
      number = number * 10 + last - '0';
    }
    while (Character.isDigit(last) || last == ';') {
      last = in.read();
    }
    boolean arrow = last >= FIRST_ARROW && last < FIRST_ARROW + Arrow.values().length;
    Key arrowed = arrow ? Arrow.values()[last - FIRST_ARROW] : KeyVisitor::other;
    return last == '~' ? numbered(number) : arrowed;
  }

  /** The function key whose ESC [ n ~ code this is, as xterm numbers them, or any other key. */
  private Key numbered(int code) {
    int at = Arrays.binarySearch(FUNCTION_CODES, code);
    return at >= 0 ? functionKey(at + 1) : KeyVisitor::other;
  }

  private static Key functionKey(int number) {
    return visitor -> visitor.function(number);
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
