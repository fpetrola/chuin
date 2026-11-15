package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ClickingOnTheMenuBarTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 10);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");

  @Test
  void aClickOnATitleOpensThatMenu() {
    open();
    click(11, 0);
    desktop.draw(screen);

    assertEquals("         ┌────────┐", screen.lines().get(1).substring(0, 19));
    assertEquals("         │ Acerca │", screen.lines().get(2).substring(0, 19));
    assertEquals("mmmmmmmmmfffffffmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void aClickOnAnItemOfTheOpenMenuRunsItAndClosesTheMenu() {
    open();
    click(2, 0);
    click(3, 3);
    desktop.draw(screen);

    assertEquals("║salir ║ ", screen.lines().get(6).substring(0, 9));
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void aClickOnTheBarAwayFromTheTitlesDoesNotReachTheWindowItCovers() {
    open();
    click(20, 0);
    desktop.draw(screen);

    assertEquals("║  Ok  ║ ", screen.lines().get(7).substring(0, 9));
    assertEquals("ffffff", screen.tints().get(7).substring(1, 7));
    assertEquals("                 │  No │", screen.lines().get(1));
  }

  @Test
  void aClickAwayFromTheOpenMenuClosesItAndReachesNothingElse() {
    open();
    click(2, 0);
    click(20, 2);
    desktop.draw(screen);

    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("║  Ok  ║ ", screen.lines().get(7).substring(0, 9));
    assertEquals("ffffff", screen.tints().get(7).substring(1, 7));
  }

  /** A bar with Archivo (Nuevo, Salir) and Ayuda (Acerca); a window under the bar's right end, and the focused one below. */
  private void open() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Archivo");
    Menu help = toolkit.menu("Ayuda");
    file.add("Nuevo", () -> said.text("nuevo"));
    file.add("Salir", () -> said.text("salir"));
    help.add("Acerca", () -> said.text("acerca"));
    bar.add(file);
    bar.add(help);
    desktop.show(bar);
    Window covered = toolkit.window("C");
    covered.place(toolkit.area(17, 0, 7, 4));
    covered.add(toolkit.button("No"), toolkit.point(0, 0));
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 5, 8, 4));
    window.add(said, toolkit.point(0, 0));
    window.add(toolkit.button("Ok"), toolkit.point(0, 1));
    desktop.open(covered);
    desktop.open(window);
    desktop.draw(screen);
  }

  private void click(int column, int row) {
    String at = "0;" + (column + 1) + ";" + (row + 1);
    toolkit.keyboard(new ByteArrayInputStream(("\u001b[<" + at + "M").getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
