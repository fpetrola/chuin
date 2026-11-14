package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** As in Turbo C++'s dialogs, options and check boxes sit on cyan, their marked letter yellow on cyan ('q'). */
class SeeingClustersLikeTurboCTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 6);
  private final Desktop desktop = toolkit.desktop();

  @Test
  void anOptionShowsItsMarkedLetterYellowOnCyanAndACheckBoxSitsOnCyanToo() {
    Window window = toolkit.window("P");
    window.place(toolkit.area(0, 0, 24, 6));
    window.add(toolkit.button("Ok"), toolkit.point(17, 0));
    window.add(toolkit.radioButtons(List.of("~2~5 lines", "~4~3/50 lines")), toolkit.point(0, 0));
    window.add(toolkit.checkBox("~E~ditor files"), toolkit.point(0, 3));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals("║(•) 25 lines   ", screen.lines().get(1).substring(0, 16));
    assertEquals("wccccqccccccccccw", screen.tints().get(1).substring(0, 17));
    assertEquals("║[ ] Editor files", screen.lines().get(4).substring(0, 17));
    assertEquals("wccccqcccccccccccw", screen.tints().get(4).substring(0, 18));
  }

  @Test
  void altAndTheLetterOfAnOptionFocusesTheGroupAndChoosesItAndOfACheckBoxFocusesAndChecksIt() {
    Window window = toolkit.window("P");
    window.place(toolkit.area(0, 0, 24, 6));
    window.add(toolkit.button("Ok"), toolkit.point(17, 0));
    window.add(toolkit.radioButtons(List.of("~N~ew window", "~C~urrent")), toolkit.point(0, 0));
    window.add(toolkit.checkBox("~E~ditor files"), toolkit.point(0, 3));
    desktop.open(window);

    press("\u001bc");
    desktop.draw(screen);
    assertEquals("║( ) New window ", screen.lines().get(1).substring(0, 16));
    assertEquals("║(•) Current    ", screen.lines().get(2).substring(0, 16));
    assertEquals("wffffffffffffffwww", screen.tints().get(2).substring(0, 18).replace('k', 'f'));

    press("\u001be");
    desktop.draw(screen);
    assertEquals("║[X] Editor files", screen.lines().get(4).substring(0, 17));
    assertEquals("ffff", screen.tints().get(4).substring(1, 5));
  }

  private void press(String typed) {
    toolkit.keyboard(new java.io.ByteArrayInputStream(typed.getBytes(java.nio.charset.StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
