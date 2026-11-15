package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * A window smaller than what it holds shows part of it, from the top left; the focus going to a view whose first cell
 * is out of sight scrolls the window just enough to show it.
 */
class ScrollingAWindowTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(10, 6);
  private final Desktop desktop = toolkit.desktop();
  private final Window window = toolkit.window("W");

  ScrollingAWindowTest() {
    window.place(toolkit.area(0, 0, 10, 5));
    window.add(toolkit.inputLine(3), toolkit.point(5, 0));
    for (int line = 0; line < 6; line++) {
      window.add(toolkit.label("l " + line), toolkit.point(0, line));
    }
    window.add(toolkit.inputLine(3), toolkit.point(0, 6));
    desktop.open(window);
    desktop.draw(screen);
  }

  @Test
  void aWindowShowsWhatFitsInsideItsFrameFromTheTop() {
    assertEquals(List.of(
        "╔══ W ═══╗",
        "║l 0     ▲",
        "║l 1     ■",
        "║l 2     ▼",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusOnAViewOutOfSightScrollsTheWindowJustEnoughToShowIt() {
    type("\tok");
    desktop.draw(screen);

    assertEquals(List.of(
        "╔══ W ═══╗",
        "║l 4     ▲",
        "║l 5     ■",
        "║ok      ▼",
        "╚════════╝"), screen.lines().subList(0, 5));
  }

  @Test
  void theFocusGoingBackToAViewAboveScrollsTheWindowBackUp() {
    type("\t");
    desktop.draw(screen);
    type("\t");
    desktop.draw(screen);

    assertEquals("║l 0     ▲", screen.lines().get(1));
  }

  @Test
  void aWindowScrollsFromItsOriginAndNeverBeforeTheTopLeft() {
    window.scroll(2, 3);
    window.scroll(-5, -1);

    assertEquals(toolkit.point(0, 2), window.origin());
  }

  /** Seven rows held in five: the right side of the frame carries ▲, a track with the thumb where the origin is, and ▼. */
  @Test
  void aWindowThatHoldsMoreRowsThanItShowsHasAScrollBarOnItsRightSide() {
    Screen tall = toolkit.screen(10, 8);
    window.place(toolkit.area(0, 0, 10, 7));
    desktop.draw(tall);

    assertEquals("▲■░░▼", rightSide(tall));

    type("\t");
    desktop.draw(tall);

    assertEquals("▲░░■▼", rightSide(tall));
    assertEquals("╚════════╝", tall.lines().get(6));
  }

  @Test
  void aWindowThatShowsAllItHoldsHasNoScrollBar() {
    Screen tall = toolkit.screen(10, 9);
    window.place(toolkit.area(0, 0, 10, 9));
    desktop.draw(tall);

    assertEquals("║║║║║║║", rightSide(tall));
  }

  /** Seven rows in five: the window scrolls at most two. ▼ and ▲ move one row; the track, a page, as far as it goes. */
  @Test
  void clickingTheArrowsAndTheTrackOfTheRightBarScrollsTheWindow() {
    Screen tall = toolkit.screen(10, 8);
    window.place(toolkit.area(0, 0, 10, 7));
    desktop.draw(tall);

    desktop.click(toolkit.point(9, 5));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 1), window.origin());

    desktop.click(toolkit.point(9, 1));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 0), window.origin());

    desktop.click(toolkit.point(9, 4));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 2), window.origin());
    assertEquals("▲░░■▼", rightSide(tall));

    desktop.click(toolkit.point(9, 5));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 2), window.origin());

    desktop.click(toolkit.point(9, 2));
    desktop.draw(tall);
    assertEquals(toolkit.point(0, 0), window.origin());
  }

  @Test
  void clickingTheRightArrowOfTheBottomBarScrollsTheWindowAColumn() {
    window.add(toolkit.label("una linea larga"), toolkit.point(0, 1));
    desktop.draw(screen);

    desktop.click(toolkit.point(8, 4));
    desktop.draw(screen);

    assertEquals(toolkit.point(1, 0), window.origin());
  }

  @Test
  void aClickOnTheFrameOfAWindowThatShowsAllItHoldsScrollsNothing() {
    Screen tall = toolkit.screen(10, 9);
    window.place(toolkit.area(0, 0, 10, 9));
    desktop.draw(tall);

    desktop.click(toolkit.point(9, 7));

    assertEquals(toolkit.point(0, 0), window.origin());
  }

  /** A button's shadow falls past it; with the button on the last row and column of the inside, everything still fits. */
  @Test
  void aButtonWhoseShadowFallsOnTheFrameGivesNoScrollBar() {
    Toolkit own = new TextToolkit();
    Desktop alone = own.desktop();
    Window snug = own.window("B");
    Screen small = own.screen(10, 4);
    snug.place(own.area(0, 0, 8, 3));
    snug.add(own.button("Ok"), own.point(0, 0));
    alone.open(snug);
    alone.draw(small);

    assertEquals(List.of(
        "╔═ B ══╗  ",
        "║  Ok  ║  ",
        "╚══════╝  "), small.lines().subList(0, 3));
  }

  /** The bars of a window holding a row more than it shows take a click on ▼; one on the window's inside goes otherwise. */
  @Test
  void theBarsOfAWindowTellWhetherAClickFellOnThemAndScrollIt() {
    Window held = toolkit.window("H");
    held.place(toolkit.area(0, 0, 10, 4));
    List<ClickTarget> targets = new java.util.ArrayList<>();
    new ScrollBars(toolkit.screen(10, 6), held, targets).write(1, 3, "mas abajo");
    ClickTarget bars = targets.getFirst();

    StringBuilder otherwise = new StringBuilder();

    bars.click(toolkit.point(3, 1), () -> otherwise.append("inside "));
    bars.click(toolkit.point(9, 2), () -> otherwise.append("bar "));

    assertEquals("inside ", otherwise.toString());
    assertEquals(toolkit.point(0, 1), held.origin());
  }

  /** The cells of the window's right side between its corners. */
  private static String rightSide(Screen screen) {
    StringBuilder side = new StringBuilder();
    List<String> lines = screen.lines();
    for (int row = 1; row < lines.size() && lines.get(row).charAt(9) != '╝'; row++) {
      side.append(lines.get(row).charAt(9));
    }
    return side.toString();
  }

  private void type(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
