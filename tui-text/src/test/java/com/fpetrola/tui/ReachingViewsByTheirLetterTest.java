package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Alt with a letter reaches what shows it highlighted: a menu first, then a button or the view a label is linked to. */
class ReachingViewsByTheirLetterTest {
  private static final String F10 = "\u001b[21~";
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 9);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");
  private final InputLine city = toolkit.inputLine(8);
  private final Window window = toolkit.window("W");

  @Test
  void altAndTheLetterOfAMenuTitleOpensThatMenu() {
    open();
    press("\u001bA");
    desktop.draw(screen);

    assertEquals("whwwwwwfkffffffffwwwwwww", screen.tints().get(0));
  }

  @Test
  void withAMenuOpenTheLetterOfAnItemRunsItAndClosesTheMenu() {
    open();
    press(F10 + "n");
    desktop.draw(screen);

    assertEquals("neu", label());
    assertEquals("wwwwwwwwwwwwwwwwwwwwwwww", screen.tints().get(0).replace('h', 'w'));
  }

  @Test
  void altAndTheLetterOfAButtonFocusesAndPressesItInUpperOrLowerCase() {
    open();
    press("\u001bn");

    assertEquals("no", label());
    desktop.draw(screen);
    assertEquals("ffkfff", screen.tints().get(7).substring(10, 16));
  }

  @Test
  void altAndTheLetterOfALinkedLabelFocusesItsViewSoTypingGoesThere() {
    open();
    press("\t\u001boBa");
    desktop.draw(screen);

    assertEquals("Ba", city.text());
    assertEquals(Optional.of(toolkit.point(9, 5)), screen.cursor());
  }

  @Test
  void altAndALetterNothingShowsDoesNothing() {
    open();
    press("\u001bz");
    desktop.draw(screen);

    assertEquals("", label());
    assertEquals(Optional.of(toolkit.point(7, 5)), screen.cursor());
  }

  @Test
  void altAndAnUpperCaseLetterReachesTheSameButton() {
    open();
    press("\u001bN");

    assertEquals("no", label());
  }

  @Test
  void aMenuTitleWinsOverAButtonWithTheSameLetter() {
    open();
    Button print = toolkit.button("~D~ruck");
    print.onPress(pressed -> said.text("druck"));
    window.add(print, toolkit.point(0, 0));
    press("\u001bd");
    desktop.draw(screen);

    assertEquals("", label());
    assertEquals("fkfffffwhwwwwwwwwwwwwwww", screen.tints().get(0));
  }

  @Test
  void withAMenuOpenAltAndTheLetterOfAnotherTitleOpensThatOne() {
    open();
    press(F10 + "\u001ba");
    desktop.draw(screen);

    assertEquals("whwwwwwfkffffffffwwwwwww", screen.tints().get(0));
  }

  /** A bar with ~D~atei (~N~eu) and ~A~ktionen; a window with ~O~rt: linked to an input line, and buttons ~S~i and ~N~o. */
  private void open() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("~D~atei");
    file.add("~N~eu", () -> said.text("neu"));
    bar.add(file);
    Menu actions = toolkit.menu("~A~ktionen");
    actions.add("~L~aden", () -> said.text("laden"));
    bar.add(actions);
    desktop.show(bar);
    window.place(toolkit.area(0, 1, 24, 8));
    window.add(said, toolkit.point(0, 0));
    window.add(toolkit.label("~O~rt:", city), toolkit.point(0, 3));
    window.add(city, toolkit.point(6, 3));
    Button yes = toolkit.button("~S~i");
    Button no = toolkit.button("~N~o");
    window.add(yes, toolkit.point(0, 5));
    window.add(no, toolkit.point(9, 5));
    yes.onPress(pressed -> said.text("si"));
    no.onPress(pressed -> said.text("no"));
    desktop.open(window);
  }

  private String label() {
    return said.text();
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
