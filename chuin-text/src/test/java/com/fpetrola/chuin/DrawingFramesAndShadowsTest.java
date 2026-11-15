package com.fpetrola.chuin;

import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.screen.Screen;

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

  @Test
  void aBorderTakesACellOnEachSideOfWhatItHolds() {
    Area held = Line.SINGLE.inside(new TextArea(2, 1, 10, 5));
    Area framed = Line.DOUBLE.around(new TextArea(3, 2, 8, 3));

    assertEquals(List.of(3, 2, 8, 3), List.of(held.column(), held.row(), held.columns(), held.rows()));
    assertEquals(List.of(2, 1, 10, 5), List.of(framed.column(), framed.row(), framed.columns(), framed.rows()));
  }

  @Test
  void aFramePlacedAtACornerHoldsWhatItHoldsFromTheCellInsideTheCorner() {
    Area held = Line.SINGLE.holding(new TextPoint(4, 2), new TextSize(6, 3));

    assertEquals(List.of(5, 3, 6, 3), List.of(held.column(), held.row(), held.columns(), held.rows()));
  }
}
