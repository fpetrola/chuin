package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DrawingFramesAndShadowsTest {
  private final Screen screen = new TextToolkit().screen(8, 5);

  @Test
  void aRaisedFrameDrawsItsBorderTitleAndInsideInItsTintAndCastsAShadow() {
    Area area = new TextArea(0, 0, 6, 3);
    Drawing raised = new RaisedFrame(area, "A", TintRole.WINDOW, Line.SINGLE);

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
    Drawing frame = new Frame(new TextArea(1, 1, 4, 3), "", TintRole.WINDOW, Line.SINGLE);

    frame.drawOn(screen);

    assertEquals(List.of("        ", " ┌──┐   ", " │  │   ", " └──┘   ", "        "), screen.lines());
  }

  @Test
  void aDoubledFrameIsDrawnWithDoubleLines() {
    Border border = Line.DOUBLE;
    new Frame(new TextArea(1, 1, 4, 3), "", TintRole.WINDOW, border).drawOn(screen);

    assertEquals("╔══╗", border.top("", 2));
    assertEquals("║  ║", border.middle(2));
    assertEquals("╚══╝", border.bottom(2));

    assertEquals(List.of("        ", " ╔══╗   ", " ║  ║   ", " ╚══╝   ", "        "), screen.lines());
  }
}
