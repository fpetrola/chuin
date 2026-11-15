package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.view.Window;

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
