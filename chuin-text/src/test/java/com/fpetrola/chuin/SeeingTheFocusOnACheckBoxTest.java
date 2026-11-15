package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

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
    String okBeforeTab = screen.tints().get(1).substring(2, 8);
    desktop.type(toolkit.tabKey());
    desktop.draw(screen);

    assertEquals(List.of("║   OK  ▄          ║", "║  [ ] Recordar    ║"), beforeTab.subList(1, 3));
    assertEquals("ffffff", okBeforeTab);
    assertEquals(List.of("║   OK  ▄          ║", "║ >[ ] Recordar    ║"), screen.lines().subList(1, 3));
    assertEquals("bbbbbb", screen.tints().get(1).substring(2, 8));
  }
}
