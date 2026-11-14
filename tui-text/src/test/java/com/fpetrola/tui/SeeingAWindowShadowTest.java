package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeeingAWindowShadowTest {
  @Test
  void aWindowCastsAShadowOneCellRightAndOneCellDown() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "┌─ A ──┐    ",
        "│      │░   ",
        "└──────┘░   ",
        " ░░░░░░░░   ",
        "            "), screen.lines());
  }
}
