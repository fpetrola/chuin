package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeeingTheFocusTest {
  @Test
  void onlyTheFocusedButtonIsDrawnBetweenBracketsAndTabMovesThem() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(toolkit.button("Sí"), toolkit.point(1, 0));
    window.add(toolkit.button("No"), toolkit.point(8, 0));
    desktop.open(window);
    desktop.draw(screen);
    List<String> beforeTab = screen.lines();
    desktop.type(toolkit.keys().tab());
    desktop.draw(screen);

    assertEquals(List.of(
        "┌─────── A ────────┐",
        "│ [ Sí ]   No      │",
        "└──────────────────┘"), beforeTab);
    assertEquals(List.of(
        "┌─────── A ────────┐",
        "│   Sí   [ No ]    │",
        "└──────────────────┘"), screen.lines());
  }
}
