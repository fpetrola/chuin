package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** A button is its caption on a green face, with a shadow of half blocks; the focused one is told by its colour alone. */
class SeeingAButtonTest {
  @Test
  void aButtonHasAGreenFaceAndAHalfBlockShadowAndTheFocusedOneABrightFace() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 20, 5));
    window.add(toolkit.button("Si"), toolkit.point(1, 0));
    window.add(toolkit.button("No"), toolkit.point(10, 0));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals(List.of(
        "║   Si  ▄    No  ▄ ║",
        "║  ▀▀▀▀▀▀   ▀▀▀▀▀▀ ║"), screen.lines().subList(1, 3));
    assertEquals(List.of(
        "wwffffffwwwbbbbbbwww",
        "wwwwwwwwwwwwwwwwwwww"), screen.tints().subList(1, 3));
  }

  @Test
  void aButtonShadowOnlyFallsOnBlankCellsSoItNeverCoversWhatAnotherViewDrew() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 20, 4));
    window.add(toolkit.label("abcd"), toolkit.point(1, 1));
    window.add(toolkit.label("xy"), toolkit.point(6, 0));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals("║  Ok  xy          ║", screen.lines().get(1));
    assertEquals("║ abcd▀▀           ║", screen.lines().get(2));
  }

  @Test
  void aButtonShadowDoesNotFallOnTheFieldOfAnInputLineUnderIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 20, 4));
    window.add(toolkit.inputLine(8), toolkit.point(0, 1));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals("║                  ║", screen.lines().get(2));
    assertEquals("wiiiiiiiiwwwwwwwwwww", screen.tints().get(2));
  }
}
