package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class TogglingACheckBoxTest {
  @Test
  void spaceOnTheFocusedCheckBoxTogglesTheMarkItIsDrawnWith() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 3);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    CheckBox remember = toolkit.checkBox("Recordar");

    window.place(toolkit.area(0, 0, 20, 3));
    window.add(remember, toolkit.point(1, 0));
    desktop.open(window);
    desktop.draw(screen);
    List<String> before = screen.lines();
    desktop.type(toolkit.spaceKey());
    desktop.draw(screen);

    assertEquals("║>[ ] Recordar     ║", before.get(1));
    assertEquals("║>[X] Recordar     ║", screen.lines().get(1));
  }
}
