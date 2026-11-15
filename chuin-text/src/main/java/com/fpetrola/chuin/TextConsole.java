package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.screen.Terminal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/** Runs a desktop on a console: turns the mouse on, shows the desktop at start and again after every key or click, and turns the mouse off when done. */
class TextConsole implements Console {
  private static final String MOUSE_ON = "\u001b[?1000h\u001b[?1002h\u001b[?1006h";
  /** Turns the mouse off and shows the cursor again, as the console was before. */
  private static final String MOUSE_OFF = "\u001b[?1006l\u001b[?1002l\u001b[?1000l\u001b[?25h";
  private final Keyboard keyboard;
  private final Terminal terminal;
  private final OutputStream out;
  private final Screen screen;

  TextConsole(Keyboard keyboard, Terminal terminal, OutputStream out, Screen screen) {
    this.keyboard = keyboard;
    this.terminal = terminal;
    this.out = out;
    this.screen = screen;
  }

  @Override
  public void run(Desktop desktop) {
    Desktop shown = new ShownDesktop(desktop, terminal, screen);
    send(MOUSE_ON);
    try {
      shown.draw(screen);
      keyboard.typeInto(shown);
    } finally {
      send(MOUSE_OFF);
    }
  }

  private void send(String sequence) {
    try {
      out.write(sequence.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
