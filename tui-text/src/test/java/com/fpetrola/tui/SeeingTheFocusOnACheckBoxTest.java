package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeeingTheFocusOnACheckBoxTest {
  @Test
  void aFocusedCheckBoxIsDrawnWithAPointerJustBeforeIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 20, 4));
    window.add(toolkit.button("OK"), toolkit.point(1, 0));
    window.add(toolkit.checkBox("Recordar"), toolkit.point(2, 1));
    desktop.open(window);
    desktop.draw(screen);
    List<String> beforeTab = screen.lines();
    desktop.type(toolkit.keys().tab());
    desktop.draw(screen);

    assertEquals(List.of("│ [ OK ]           │", "│  [ ] Recordar    │"), beforeTab.subList(1, 3));
    assertEquals(List.of("│   OK             │", "│ >[ ] Recordar    │"), screen.lines().subList(1, 3));
  }
}
