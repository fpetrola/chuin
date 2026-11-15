package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class WritingOnAScreenTest {
  @Test
  void aNewScreenIsBlankAndWrittenTextLandsWhereItWasWritten() {
    Screen screen = new TextToolkit().screen(6, 2);

    screen.write(1, 1, "hola");

    assertEquals(List.of("      ", " hola "), screen.lines());
  }

  @Test
  void aNewScreenIsUntintedAndPaintedCellsKeepTheirTintEvenWhereThePaintFallsOutside() {
    Screen screen = new TextToolkit().screen(6, 2);

    screen.paint(-1, 0, 3, 'w');
    screen.paint(4, 1, 5, 's');
    screen.paint(0, 2, 6, 'f');

    assertEquals(List.of("ww....", "....ss"), screen.tints());
  }
}
