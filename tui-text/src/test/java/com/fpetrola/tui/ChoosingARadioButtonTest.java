package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ChoosingARadioButtonTest {
  private static final String DOWN = "\u001b[B";
  private static final String UP = "\u001b[A";

  @Test
  void theArrowsChooseOneOptionOfAFocusedGroupAndStopAtBothEnds() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 6);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("R");
    RadioButtons sizes = toolkit.radioButtons(List.of("Uno", "Dos", "Tres"));

    window.place(toolkit.area(0, 0, 12, 5));
    window.add(sizes, toolkit.point(1, 0));
    desktop.open(window);
    press(toolkit, desktop, UP + DOWN + DOWN + DOWN + DOWN + UP);
    desktop.draw(screen);

    assertEquals(List.of(
        "║ ( ) Uno  ║  ",
        "║ (•) Dos  ║  ",
        "║ ( ) Tres ║  "), screen.lines().subList(1, 4));
    assertEquals("wwffffffffwws.", screen.tints().get(2));
  }

  @Test
  void aGroupOnlyTakesTheArrowsAfterTabReachesIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(14, 7);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("R");

    window.place(toolkit.area(0, 0, 12, 5));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(toolkit.radioButtons(List.of("Si", "No")), toolkit.point(0, 1));
    desktop.open(window);
    press(toolkit, desktop, DOWN);
    desktop.draw(screen);

    assertEquals("╔═══ R ════╗  ", screen.lines().get(1));
    assertEquals(List.of("wwwwwwwwwwwws.", "wwwwwwwwwwwws."), screen.tints().subList(3, 5));

    press(toolkit, desktop, "\t" + DOWN);
    desktop.draw(screen);

    assertEquals("╔═══ R ════╗  ", screen.lines().get(1));
    assertEquals(List.of("wwwwwwwwwwwws.", "wffffffwwwwws."), screen.tints().subList(3, 5));
    assertEquals("║( ) Si    ║  ", screen.lines().get(3));
    assertEquals("║(•) No    ║  ", screen.lines().get(4));
  }

  @Test
  void aGroupNeedsAtLeastOneOption() {
    assertThrows(IllegalArgumentException.class, () -> new TextToolkit().radioButtons(List.of()));
  }

  @Test
  void aGroupCountsAsOneStopOnTheWayToTheViewsAfterIt() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 8);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Label said = toolkit.label("");
    Button ok = toolkit.button("Ok");

    window.place(toolkit.area(0, 0, 20, 8));
    window.add(toolkit.radioButtons(List.of("a", "b", "c")), toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 3));
    window.add(said, toolkit.point(0, 4));
    ok.onPress(pressed -> said.text("ok"));
    desktop.open(window);
    press(toolkit, desktop, "\r\t");
    desktop.draw(screen);
    assertEquals("║                  ║", screen.lines().get(5));

    press(toolkit, desktop, "\r");
    desktop.draw(screen);
    assertEquals("║ok                ║", screen.lines().get(5));
  }

  private static void press(Toolkit toolkit, Desktop desktop, String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
