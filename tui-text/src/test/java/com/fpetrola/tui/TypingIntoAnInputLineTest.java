package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class TypingIntoAnInputLineTest {
  @Test
  void lettersTypedOnTheFocusedInputLineAreShownInItsField() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(10);
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("hola".getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(name, toolkit.point(1, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║ hola             ║", screen.lines().get(1));
  }

  @Test
  void escapeSequencesTheKeyboardDoesNotKnowTypeNothing() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(10);
    String home = "\u001b[H";
    String delete = "\u001b[3~";
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream(("a" + home + "b" + delete + "c").getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(name, toolkit.point(1, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║ abc              ║", screen.lines().get(1));
  }
}
