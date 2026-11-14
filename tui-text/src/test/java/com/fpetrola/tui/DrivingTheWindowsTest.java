package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** The windows of a desktop, on their own: they open, take keys and clicks, and draw, with no menu bar around them. */
class DrivingTheWindowsTest {
  @Test
  void windowsOpenTakeKeysAndClicksAndDrawWithoutAMenuBar() {
    Toolkit toolkit = new TextToolkit();
    Windows windows = new TextDesktop();
    Screen screen = toolkit.screen(20, 4);
    Window window = toolkit.window("A");
    Label said = toolkit.label("");
    Button yes = toolkit.button("Si");
    Button no = toolkit.button("No");
    yes.onPress(pressed -> said.text("si"));
    no.onPress(pressed -> said.text("no"));
    window.place(toolkit.area(0, 0, 20, 4));
    window.add(yes, toolkit.point(0, 0));
    window.add(no, toolkit.point(8, 0));
    window.add(said, toolkit.point(0, 1));

    windows.open(window);
    windows.type(toolkit.keys().enter());
    windows.draw(screen);
    assertEquals("si", said.text());

    windows.click(toolkit.point(10, 1));
    windows.draw(screen);
    assertEquals("no", said.text());
    assertEquals("╔═══════ A ════════╗", screen.lines().getFirst());
  }
}
