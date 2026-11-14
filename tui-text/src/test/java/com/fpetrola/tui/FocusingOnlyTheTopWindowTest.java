package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class FocusingOnlyTheTopWindowTest {
  @Test
  void onlyTheWindowOnTopShowsItsFocusedView() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");

    below.place(toolkit.area(0, 0, 9, 3));
    below.add(toolkit.button("No"), toolkit.point(0, 0));
    above.place(toolkit.area(10, 0, 9, 3));
    above.add(toolkit.button("Si"), toolkit.point(0, 0));
    desktop.open(below);
    desktop.open(above);
    desktop.draw(screen);

    assertEquals("│  No   │░│[ Si ] │░", screen.lines().get(1));
    assertEquals("wwwwwwwwwswffffffwws", screen.tints().get(1));
  }
}
