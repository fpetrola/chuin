package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * A text marks one letter between tildes, "~D~atei", and is drawn without them, the marked letter in a colour of its
 * own: 'h' on grey (menus and windows), 'k' on green (buttons and the chosen menu item). "~~" is a tilde.
 */
class SeeingHighlightedLettersTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 8);
  private final Desktop desktop = toolkit.desktop();

  @Test
  void menuTitlesAndItemsShowTheirMarkedLetter() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("~D~atei");
    file.add("~N~eu", () -> { });
    file.add("Be~e~nden", () -> { });
    bar.add(file);
    bar.add(toolkit.menu("~A~ktionen"));
    desktop.show(bar);
    toolkit.keyboard(new ByteArrayInputStream("\u001b[21~".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals(" Datei  Aktionen       ", screen.lines().get(0).substring(0, 23));
    assertEquals("fkfffffmhmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("│ Neu     │", screen.lines().get(2).substring(0, 11));
    assertEquals("mfkfffffff", screen.tints().get(2).substring(0, 10));
    assertEquals("│ Beenden │", screen.lines().get(3).substring(0, 11));
    assertEquals("mmmmhmmmmm", screen.tints().get(3).substring(0, 10));
  }

  @Test
  void buttonsAndLabelsShowTheirMarkedLetterAndATildeIsWrittenAsTwo() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 24, 6));
    window.add(toolkit.label("~O~rt: a~~b"), toolkit.point(0, 0));
    window.add(toolkit.button("~N~eu"), toolkit.point(0, 2));
    window.add(toolkit.button("Zur~ü~ck"), toolkit.point(8, 2));
    desktop.open(window);
    desktop.draw(screen);

    assertEquals("║Ort: a~b              ║", screen.lines().get(1));
    assertEquals("whwwwwwwwwwwwwwwwwwwwwww", screen.tints().get(1));
    assertEquals("║  Neu  ▄  Zurück  ▄   ║", screen.lines().get(3));
    assertEquals("wffkffffwbbbbbkbbbbwwwww", screen.tints().get(3));
  }
}
