package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeeingAWindowShadowTest {
  @Test
  void aWindowCastsAShadowOneCellRightAndOneCellDownThatGreysWhatIsUnderIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "╔═ A ══╗    ",
        "║      ║    ",
        "╚══════╝    ",
        "            ",
        "            "), screen.lines());
    assertEquals(List.of(
        "wwwwwwww....",
        "wwwwwwwws...",
        "wwwwwwwws...",
        ".ssssssss...",
        "............"), screen.tints());
  }

  @Test
  void aShadowOverAnotherWindowLeavesItsCharactersAndGreysThem() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");

    below.place(toolkit.area(0, 0, 12, 5));
    below.add(toolkit.label("abcdefgh"), toolkit.point(0, 2));
    above.place(toolkit.area(0, 0, 6, 3));
    desktop.open(below);
    desktop.open(above);
    desktop.draw(screen);

    assertEquals("│abcdefgh  │", screen.lines().get(3));
    assertEquals("wsssssswwwww", screen.tints().get(3));
    assertEquals("wwwwwwswwwww", screen.tints().get(1));
  }
}
