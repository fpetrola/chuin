package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TintingCellsTest {
  @Test
  void aTintPaintsCellsOfARowInItsLetter() {
    Screen screen = new TextToolkit().screen(6, 1);
    Tint focus = TintRole.FOCUS;

    focus.paint(screen, 1, 0, 3);

    assertEquals('f', focus.letter());
    assertEquals(".fff..", screen.tints().getFirst());
  }
}
