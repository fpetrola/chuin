package com.fpetrola.tui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/** Reads bytes from a stream until it ends or Ctrl+Q is typed, and types each as a key. */
class StreamKeyboard implements Keyboard {
  private static final int END = -1;
  private static final int CONTROL_Q = 0x11;
  private static final int CARRIAGE_RETURN = '\r';
  private static final int TAB = '\t';
  private static final int FIRST_ARROW = 'A';
  /** The ESC [ n ~ codes of F1 to F12, in order: xterm skips 16 and 22. */
  private static final int[] FUNCTION_CODES = {11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 23, 24};
  /** A mouse report carries the button, the column and the row. */
  private static final int MOUSE_NUMBERS = 3;
  private static final int LEFT_BUTTON = 0;
  private static final int PRESSED = 'M';
  private static final int RELEASED = 'm';
  /** What a mouse report adds to the button when the pointer moved with it held. */
  private static final int MOVED = 32;
  private static final int ESCAPE = 0x1B;
  /** No byte: nothing came, or nothing is put back. */
  private static final int NONE = -2;
  /** How long to wait after an escape for the rest of a sequence: Turbo Vision and ncurses wait about as long. */
  private static final long ESCAPE_WAIT_NANOS = 50_000_000L;
  /** How often to look again for the rest of a sequence while waiting. */
  private static final long ONE_MILLISECOND = 1_000_000L;
  private static final int BACKSPACE = 0x7F;
  /** What several terminals send for backspace instead of 0x7F. */
  private static final int CONTROL_H = 0x08;
  private static final int CONTROL_A = 0x01;
  private static final int CONTROL_Z = 0x1A;
  private static final int SPACE = ' ';
  private static final int FIRST_PRINTABLE = '!';
  private static final int LAST_PRINTABLE = '~';
  private final InputStream in;

  /** Where the keys a program binds by name come from, so the keyboard reads the same ones. */
  private final Toolkit keys;

  StreamKeyboard(InputStream in, Toolkit keys) {
    this.keys = keys;
    this.in = in;
  }

  @Override
  public void typeInto(Desktop desktop) {
    try {
      for (int read = next(); read != END && read != CONTROL_Q; read = next()) {
        heard(read).accept(desktop);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Reads the rest of an escape sequence: ESC [ then digits and ';' up to a final byte. Only the arrows mean something. */
  /** What a byte, and the bytes after it when it starts an escape sequence, does to the desktop. */
  private Consumer<Desktop> heard(int read) throws IOException {
    Key key = read == ESCAPE ? KeyVisitor::other : keyFor(read);
    return read == ESCAPE ? escaped() : desktop -> desktop.type(key);
  }

  /** The byte put back after an escape, read again before the stream, or none. */
  private int pending = NONE;

  private int next() throws IOException {
    int read = pending == NONE ? in.read() : pending;
    pending = NONE;
    return read;
  }

  /** The byte that comes within a short wait after an escape, or none when nothing does. */
  private int waited() throws IOException {
    long deadline = System.nanoTime() + ESCAPE_WAIT_NANOS;
    while (in.available() == 0 && System.nanoTime() < deadline) {
      LockSupport.parkNanos(ONE_MILLISECOND);
    }
    return in.available() > 0 ? in.read() : NONE;
  }

  /** What follows an escape: a sequence after [ or O, Alt with an ASCII letter, or else the escape key alone, the byte after it read as usual. */
  private Consumer<Desktop> escaped() throws IOException {
    int kind = waited();
    boolean sequence = kind == '[' || kind == 'O';
    boolean letter = kind >= 'a' && kind <= 'z' || kind >= 'A' && kind <= 'Z';
    Key alone = letter ? visitor -> visitor.alt((char) kind) : KeyVisitor::escape;
    pending = sequence || letter ? NONE : kind;
    return sequence ? sequence(kind) : desktop -> desktop.type(alone);
  }

  private Consumer<Desktop> sequence(int kind) throws IOException {
    int first = kind == '[' ? in.read() : END;
    Key key = kind == 'O' ? shifted(in.read()) : KeyVisitor::other;
    Key bracketed = kind == '[' && first != '<' ? bracketed(first) : key;
    return first == '<' ? mouse() : desktop -> desktop.type(bracketed);
  }

  /** ESC [ < button ; column ; row then M when pressed or moved and m when released, counted from 1: the left button's press is a click, its moves held a drag, its letting go a release. */
  private Consumer<Desktop> mouse() throws IOException {
    int[] numbers = new int[MOUSE_NUMBERS];
    int last = in.read();
    for (int at = 0; at < MOUSE_NUMBERS; at++) {
      for (; Character.isDigit(last); last = in.read()) {
        numbers[at] = numbers[at] * 10 + last - '0';
      }
      last = at < MOUSE_NUMBERS - 1 ? in.read() : last;
    }
    boolean left = numbers[0] == LEFT_BUTTON;
    boolean dragged = numbers[0] == LEFT_BUTTON + MOVED && last == PRESSED;
    Point cell = new TextPoint(numbers[1] - 1, numbers[2] - 1);
    Consumer<Desktop> other = desktop -> desktop.type(KeyVisitor::other);
    Consumer<Desktop> moved = dragged ? desktop -> desktop.drag(cell) : other;
    Consumer<Desktop> released = left && last == RELEASED ? desktop -> desktop.release(cell) : moved;
    return left && last == PRESSED ? desktop -> desktop.click(cell) : released;
  }

  /** ESC O P..S: how many terminals send F1 to F4. */
  private Key shifted(int last) {
    boolean function = last >= 'P' && last <= 'S';
    return function ? functionKey(last - 'P' + 1) : KeyVisitor::other;
  }

  /** ESC [ then a number, maybe more numbers after ';', and a final byte: ~ for function keys, A..D for arrows. */
  private Key bracketed(int first) throws IOException {
    int number = 0;
    int last = first;
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
    boolean control = read >= CONTROL_A && read <= CONTROL_Z;
    Key controlled = control ? visitor -> visitor.control((char) ('a' + read - CONTROL_A)) : KeyVisitor::other;
    Key written = printable ? visitor -> visitor.character((char) read) : controlled;
    boolean erasing = read == BACKSPACE || read == CONTROL_H;
    Key erased = erasing ? KeyVisitor::backspace : written;
    Key spaced = read == SPACE ? keys.spaceKey() : erased;
    Key named = read == TAB ? keys.tabKey() : spaced;
    return read == CARRIAGE_RETURN ? keys.enterKey() : named;
  }
}
