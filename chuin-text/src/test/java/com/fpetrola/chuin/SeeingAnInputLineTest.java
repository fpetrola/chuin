package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** An input line is its text on a field of its own colour as wide as the line, focused or not. */
class SeeingAnInputLineTest {
  @Test
  void anInputLineIsItsTextOnAFieldAsWideAsTheLineWhetherFocusedOrNot() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(6);
    InputLine city = toolkit.inputLine(4);

    window.place(toolkit.area(0, 0, 20, 4));
    window.add(name, toolkit.point(0, 0));
    window.add(city, toolkit.point(0, 1));
    name.type('A');
    name.type('n');
    name.type('a');
    desktop.open(window);
    desktop.draw(screen);

    assertEquals("║Ana               ║", screen.lines().get(1));
    assertEquals("wiiiiiiwwwwwwwwwwwww", screen.tints().get(1));
    assertEquals("wiiiiwwwwwwwwwwwwwww", screen.tints().get(2));
  }
}
