package com.fpetrola.tui;

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
}
