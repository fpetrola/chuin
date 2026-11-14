package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ErasingWithBackspaceTest {
  @Test
  void backspaceErasesTheLastLetterOfTheFocusedInputLine() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(10);
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("holax\u007f".getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(name, toolkit.point(1, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║ hola______       ║", screen.lines().get(1));
  }

  @Test
  void backspaceOnAnEmptyInputLineLeavesItEmpty() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(10);
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\u007f\u007fa".getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(name, toolkit.point(1, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║ a_________       ║", screen.lines().get(1));
  }

  @Test
  void backspaceWithTheFocusOnACheckBoxChangesNothing() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    CheckBox remember = toolkit.checkBox("Ok");
    InputLine name = toolkit.inputLine(4);
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\tz\t\u007f".getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(remember, toolkit.point(1, 0));
    window.add(name, toolkit.point(10, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║>[ ] Ok   z___    ║", screen.lines().get(1));
  }
}
