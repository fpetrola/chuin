package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DrawingFramesAndShadowsTest {
  private final Screen screen = new TextToolkit().screen(8, 5);

  @Test
  void aRaisedFrameDrawsItsBorderTitleAndInsideInItsTintAndCastsAShadow() {
    Drawing raised = new RaisedFrame(new TextArea(0, 0, 6, 3), "A", 'w');

    raised.drawOn(screen);

    assertEquals(List.of(
        "┌ A ─┐  ",
        "│    │  ",
        "└────┘  ",
        "        ",
        "        "), screen.lines());
    assertEquals(List.of(
        "wwwwww..",
        "wwwwwws.",
        "wwwwwws.",
        ".ssssss.",
        "........"), screen.tints());
  }

  @Test
  void aFrameWithNoTitleIsAPlainBorder() {
    Drawing frame = new Frame(new TextArea(1, 1, 4, 3), "", 'w');

    frame.drawOn(screen);

    assertEquals(List.of("        ", " ┌──┐   ", " │  │   ", " └──┘   ", "        "), screen.lines());
  }
}
