package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DrawingAWindowTest {
  @Test
  void aWindowIsDrawnWithItsFrameTitleAndButton() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Saludo");
    Area whole = toolkit.area(0, 0, 20, 5);
    Point inside = toolkit.point(1, 1);

    window.place(whole);
    window.add(toolkit.button("OK"), inside);
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "╔═════ Saludo ═════╗",
        "║                  ║",
        "║   OK  ▄          ║",
        "║  ▀▀▀▀▀▀          ║",
        "╚══════════════════╝"), screen.lines());
  }
}
