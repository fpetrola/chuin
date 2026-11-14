package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Each cell has a tint from the palette: w window, f focused view, s shadow, . the bare desktop. */
class SeeingColorsTest {
  @Test
  void aWindowItsFocusedButtonAndItsShadowAreTintedApart() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 10, 4));
    window.add(toolkit.button("Ok"), toolkit.point(1, 0));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "wwwwwwwwww..",
        "wwffffffwws.",
        "wwwwwwwwwws.",
        "wwwwwwwwwws.",
        ".ssssssssss."), screen.tints());
  }

  @Test
  void onlyTheFocusedViewIsTintedAsFocusedAndTabMovesTheTint() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Keys keys = toolkit.keys();

    window.place(toolkit.area(0, 0, 12, 4));
    window.add(toolkit.button("Ok"), toolkit.point(1, 0));
    window.add(toolkit.checkBox("C"), toolkit.point(1, 1));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "wwwwwwwwwwww..",
        "wwffffffwwwws.",
        "wwcccccwwwwws.",
        "wwwwwwwwwwwws.",
        ".ssssssssssss."), screen.tints());

    desktop.type(keys.tab());
    desktop.draw(screen);

    assertEquals(List.of(
        "wwwwwwwwwwww..",
        "wwbbbbbbwwwws.",
        "wwfffffwwwwws.",
        "wwwwwwwwwwwws.",
        ".ssssssssssss."), screen.tints());
  }
}
