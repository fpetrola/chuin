package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class KeepingViewsInsideTheirWindowTest {
  @Test
  void whatAViewDrawsPastTheInsideOfItsWindowIsCutOff() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Label greeting = toolkit.label("Hola Bartolomeo, elegiste Verde");

    window.place(toolkit.area(0, 0, 10, 4));
    window.add(greeting, toolkit.point(0, 0));
    window.add(toolkit.label("abajo del todo"), toolkit.point(0, 5));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ A ═══╗    ",
        "║Hola Bar▲    ",
        "║        ▼    ",
        "╚◄■░░░░░►╝    ",
        "              "), screen.lines());
  }
}
