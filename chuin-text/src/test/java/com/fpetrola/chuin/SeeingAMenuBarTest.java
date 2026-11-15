package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SeeingAMenuBarTest {
  @Test
  void theMenuBarShowsTheTitlesOfItsMenusAcrossTheTopRow() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(24, 4);
    Desktop desktop = toolkit.desktop();
    MenuBar bar = toolkit.menuBar();
    Window window = toolkit.window("A");

    bar.add(toolkit.menu("Archivo"));
    bar.add(toolkit.menu("Ayuda"));
    desktop.show(bar);
    window.place(toolkit.area(0, 1, 8, 3));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(" Archivo  Ayuda         ", screen.lines().get(0));
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("╔═ A ══╗                ", screen.lines().get(1));
  }

  @Test
  void theMenuBarStaysOverAWindowThatReachesTheTopRow() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(24, 4);
    Desktop desktop = toolkit.desktop();
    MenuBar bar = toolkit.menuBar();
    Window window = toolkit.window("A");

    bar.add(toolkit.menu("Archivo"));
    bar.add(toolkit.menu("Ayuda"));
    desktop.show(bar);
    window.place(toolkit.area(0, 0, 20, 3));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(" Archivo  Ayuda         ", screen.lines().get(0));
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("║                  ║    ", screen.lines().get(1));
    assertEquals("╚══════════════════╝    ", screen.lines().get(2));
  }
}
