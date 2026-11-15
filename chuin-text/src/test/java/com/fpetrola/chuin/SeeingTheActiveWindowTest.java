package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeeingTheActiveWindowTest {
  @Test
  void theWindowOnTopHasADoubleFrameAndTheOnesBelowASingleOne() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");

    below.place(toolkit.area(0, 0, 8, 3));
    above.place(toolkit.area(10, 0, 8, 3));
    desktop.open(below);
    desktop.open(above);
    desktop.draw(screen);

    assertEquals(List.of(
        "┌─ B ──┐  ╔═ A ══╗  ",
        "│      │  ║      ║  ",
        "└──────┘  ╚══════╝  ",
        "                    "), screen.lines());
  }
}
