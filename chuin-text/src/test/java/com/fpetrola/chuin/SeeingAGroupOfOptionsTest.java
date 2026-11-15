package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** A group of options sits on a cyan field as wide as its longest option; its label above reaches it with Alt. */
class SeeingAGroupOfOptionsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 6);
  private final Desktop desktop = toolkit.desktop();

  @Test
  void aGroupIsDrawnOnACyanFieldAsWideAsItsLongestOptionWithItsLabelAbove() {
    open();
    desktop.draw(screen);

    assertEquals("║Land:             ║", screen.lines().get(1).substring(0, 20));
    assertEquals("║(•) Deutschland   ║", screen.lines().get(2).substring(0, 20));
    assertEquals("whwwwwwwwwwwwwwwwwww", screen.tints().get(1).substring(0, 20));
    assertEquals("wcccccccccccccccwwww", screen.tints().get(2).substring(0, 20));
    assertEquals("wcccccccccccccccwwww", screen.tints().get(3).substring(0, 20));
  }

  @Test
  void withTheFocusAfterAltAndTheLetterOfItsLabelTheChosenOptionIsBright() {
    open();
    toolkit.keyboard(new ByteArrayInputStream("\u001bl".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals("wfffffffffffffffwwww", screen.tints().get(2).substring(0, 20));
    assertEquals("wcccccccccccccccwwww", screen.tints().get(3).substring(0, 20));
  }

  private void open() {
    Window window = toolkit.window("W");
    OptionGroup land = toolkit.optionGroup(java.util.List.of("Deutschland", "U.S.A."));
    window.place(toolkit.area(0, 0, 20, 6));
    window.add(toolkit.button("Ok"), toolkit.point(10, 3));
    window.add(toolkit.label("~L~and:", land), toolkit.point(0, 0));
    window.add(land, toolkit.point(0, 1));
    desktop.open(window);
  }
}
