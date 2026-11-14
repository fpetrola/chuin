package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class MovingAWindowWithArrowsTest {
  @Test
  void theArrowKeysMoveTheTopWindowOneCellEach() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    String right = "\u001b[C";
    String down = "\u001b[B";
    String left = "\u001b[D";
    String up = "\u001b[A";
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream((right + right + down + down + left + up).getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);

    assertEquals(List.of(
        "            ",
        " ╔═ A ══╗   ",
        " ║      ║   ",
        " ╚══════╝   ",
        "            "), screen.lines());
  }

  @Test
  void aWindowMovedPastTheTopLeftCornerIsCutWhereTheScreenEnds() {
    List<String> lines = movedAndDrawn("\u001b[D\u001b[D\u001b[A");

    assertEquals(List.of(
        "     ║      ",
        "═════╝      ",
        "            ",
        "            ",
        "            "), lines);
  }

  @Test
  void aWindowMovedPastTheBottomRightCornerIsCutWhereTheScreenEnds() {
    String right = "\u001b[C";
    String down = "\u001b[B";
    List<String> lines = movedAndDrawn(right.repeat(6) + down.repeat(3));

    assertEquals(List.of(
        "            ",
        "            ",
        "            ",
        "      ╔═ A ═",
        "      ║     "), lines);
  }

  private List<String> movedAndDrawn(String typed) {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(12, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8)));

    window.place(toolkit.area(0, 0, 8, 3));
    desktop.open(window);
    keyboard.typeInto(desktop);
    desktop.draw(screen);
    return screen.lines();
  }

  @Test
  void drawingAgainAfterAMoveLeavesNoTrailBehind() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(10, 4);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");

    window.place(toolkit.area(0, 0, 6, 3));
    desktop.open(window);
    desktop.draw(screen);
    desktop.type(visitor -> visitor.right());
    desktop.type(visitor -> visitor.right());
    desktop.draw(screen);

    assertEquals(List.of(
        "  ╔ A ═╗  ",
        "  ║    ║  ",
        "  ╚════╝  ",
        "          "), screen.lines());
    assertEquals(List.of(
        "..wwwwww..",
        "..wwwwwws.",
        "..wwwwwws.",
        "...ssssss."), screen.tints());
  }
}
