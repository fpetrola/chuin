package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** A drop down shows its chosen item and a ↓; ↓ opens its list under it, the arrows walk it, Enter chooses, Esc closes. */
class ChoosingFromADropDownTest {
  private static final String DOWN = "\u001b[B";
  private static final String UP = "\u001b[A";
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(20, 8);
  private final Desktop desktop = toolkit.desktop();
  private final DropDown answer = toolkit.dropDown(List.of("Ja", "Nein", "Vielleicht"));

  @Test
  void aClosedDropDownShowsItsChosenItemOnAFieldAndAnArrow() {
    open();
    desktop.draw(screen);

    assertEquals("║Ja        ↓       ║", screen.lines().get(1));
    assertEquals("wiiiiiiiiiiiwwwwwww", screen.tints().get(1).substring(0, 19));
    assertEquals(Optional.of("Ja"), answer.chosen());
  }

  @Test
  void downOpensTheListUnderItWithTheChosenItemHighlighted() {
    open();
    press(DOWN);
    desktop.draw(screen);

    assertEquals(List.of(
        "║Ja        ↓       ║",
        "║┌──────────┐      ║",
        "║│Ja        │      ║",
        "║│Nein      │      ║",
        "║│Vielleicht│      ║",
        "║└──────────┘      ║"), screen.lines().subList(1, 7).stream().map(line -> line.substring(0, 20)).toList());
    assertEquals("wmffffffffffmswwwww", screen.tints().get(3).substring(0, 19));
  }

  @Test
  void theArrowsWalkTheOpenListAndEnterChoosesAndClosesIt() {
    open();
    press(DOWN + DOWN + DOWN + DOWN + UP + "\r");
    desktop.draw(screen);

    assertEquals(Optional.of("Nein"), answer.chosen());
    assertEquals("║Nein      ↓       ║", screen.lines().get(1));
    assertEquals("║                  ║", screen.lines().get(2));
  }

  @Test
  void escapeClosesTheOpenListAndKeepsTheChosenItem() {
    open();
    press(DOWN + DOWN + "\u001b");
    desktop.draw(screen);

    assertEquals(Optional.of("Ja"), answer.chosen());
    assertEquals("║                  ║", screen.lines().get(2));
  }

  @Test
  void escapeClosesAnOpenListBeforeAnyBindingButRunsTheBindingWhenNothingIsOpen() {
    Label said = toolkit.label("");
    StatusLine status = toolkit.statusLine();
    status.add("~Esc~ Raus", toolkit.keys().escape(), () -> said.text("raus"));
    desktop.show(status);
    open();
    press(DOWN + "\u001b");
    assertEquals("", said.text());

    press("\u001b");
    assertEquals("raus", said.text());
  }

  @Test
  void anOpenListIsDrawnOverEverythingEvenPastTheBottomOfItsWindow() {
    Window small = toolkit.window("S");
    DropDown low = toolkit.dropDown(List.of("Ja", "Nein"));
    small.place(toolkit.area(0, 0, 12, 3));
    small.add(low, toolkit.point(0, 0));
    desktop.open(small);
    press(DOWN);
    desktop.draw(screen);

    assertEquals(List.of(
        "║Ja  ↓     ║",
        "╚┌────┐════╝",
        " │Ja  │     ",
        " │Nein│     ",
        " └────┘     "), screen.lines().subList(1, 6).stream().map(line -> line.substring(0, 12)).toList());
  }

  @Test
  void tabClosesAnOpenListAndKeepsTheChosenItem() {
    open();
    press(DOWN + DOWN + "\t");
    desktop.draw(screen);

    assertEquals(Optional.of("Ja"), answer.chosen());
    assertEquals("║                  ║", screen.lines().get(2));
  }

  @Test
  void aClickAwayFromAnOpenListClosesIt() {
    open();
    press(DOWN);
    desktop.draw(screen);
    press("\u001b[<0;18;7M");
    desktop.draw(screen);

    assertEquals("║                  ║", screen.lines().get(2));
  }

  private void open() {
    Window window = toolkit.window("W");
    window.place(toolkit.area(0, 0, 20, 8));
    window.add(answer, toolkit.point(0, 0));
    desktop.open(window);
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
