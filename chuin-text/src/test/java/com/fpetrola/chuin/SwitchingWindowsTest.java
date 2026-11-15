package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class SwitchingWindowsTest {
  /** What a terminal sends for F6. */
  private static final String F6 = "\u001b[17~";

  @Test
  void f6BringsTheWindowBelowToTheTopAndEachWindowKeepsItsOwnFocus() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window below = toolkit.window("B");
    Window above = toolkit.window("A");

    below.place(toolkit.area(0, 0, 9, 4));
    below.add(toolkit.button("No"), toolkit.point(0, 0));
    above.place(toolkit.area(10, 0, 9, 4));
    above.add(toolkit.button("Si"), toolkit.point(0, 0));
    above.add(toolkit.button("Ya"), toolkit.point(0, 1));
    desktop.open(below);
    desktop.open(above);
    toolkit.keyboard(new ByteArrayInputStream(("\t" + F6).getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║  No  ▄║ │  Si  ▄│ ", screen.lines().get(1));
    assertEquals("║ ▀▀▀▀▀▀║ │  Ya  ▄│ ", screen.lines().get(2));
    assertEquals("ffffff", screen.tints().get(1).substring(1, 7));
    assertEquals("bbbbbb", screen.tints().get(1).substring(11, 17));

    toolkit.keyboard(new ByteArrayInputStream(F6.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals("│  No  ▄│ ║  Si  ▄║ ", screen.lines().get(1));
    assertEquals("│ ▀▀▀▀▀▀│ ║  Ya  ▄║ ", screen.lines().get(2));
    assertEquals("ffffff", screen.tints().get(2).substring(11, 17));
    assertEquals("bbbbbb", screen.tints().get(1).substring(11, 17));
  }

  @Test
  void f6RaisesTheBottomWindowOverTheOthers() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 4);
    Desktop desktop = toolkit.desktop();
    for (int at = 0; at < 3; at++) {
      Window window = toolkit.window(String.valueOf(at + 1));
      window.place(toolkit.area(2 * at, 0, 6, 3));
      desktop.open(window);
    }
    desktop.draw(screen);
    assertEquals("┌ ┌ ╔ 3 ═╗  ", screen.lines().get(0));

    press(toolkit, desktop, F6);
    desktop.draw(screen);
    assertEquals("╔ 1 ═╗3 ─┐  ", screen.lines().get(0));

    press(toolkit, desktop, F6);
    desktop.draw(screen);
    assertEquals("┌ ╔ 2 ═╗─┐  ", screen.lines().get(0));
  }

  @Test
  void f6WithOneWindowOrNoneChangesNothing() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(10, 4);
    Desktop empty = toolkit.desktop();
    Desktop single = toolkit.desktop();
    Window window = toolkit.window("A");

    press(toolkit, empty, F6);
    empty.draw(screen);
    assertEquals(List.of("          ", "          ", "          ", "          "), screen.lines());

    window.place(toolkit.area(0, 0, 8, 3));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    single.open(window);
    press(toolkit, single, F6);
    single.draw(screen);
    assertEquals("╔═ A ══╗  ", screen.lines().get(0));
    assertEquals("║  Ok  ║  ", screen.lines().get(1));
    assertEquals("ffffff", screen.tints().get(1).substring(1, 7));
  }

  private static void press(Toolkit toolkit, Desktop desktop, String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }

  @Test
  void aWindowOpenedLaterStartsOnItsFirstViewAndTheOneBelowKeepsWhereItWas() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window first = toolkit.window("B");
    Window second = toolkit.window("A");

    first.place(toolkit.area(0, 0, 9, 4));
    first.add(toolkit.button("No"), toolkit.point(0, 0));
    first.add(toolkit.button("Ni"), toolkit.point(0, 1));
    second.place(toolkit.area(10, 0, 9, 4));
    second.add(toolkit.button("Si"), toolkit.point(0, 0));
    second.add(toolkit.button("Ya"), toolkit.point(0, 1));
    desktop.open(first);
    press(toolkit, desktop, "\t");
    desktop.open(second);
    desktop.draw(screen);
    assertEquals("│  No  ▄│ ║  Si  ▄║ ", screen.lines().get(1));
    assertEquals("ffffff", screen.tints().get(1).substring(11, 17));

    press(toolkit, desktop, F6);
    desktop.draw(screen);
    assertEquals("║  No  ▄║ │  Si  ▄│ ", screen.lines().get(1));
    assertEquals("║  Ni  ▄║ │  Ya  ▄│ ", screen.lines().get(2));
    assertEquals("ffffff", screen.tints().get(2).substring(1, 7));
    assertEquals("bbbbbb", screen.tints().get(1).substring(1, 7));
  }
}
