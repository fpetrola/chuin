package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ChoosingFromAMenuTest {
  private static final String F10 = "\u001b[21~";
  private static final String UP = "\u001b[A";
  private static final String DOWN = "\u001b[B";
  private static final String RIGHT = "\u001b[C";
  private static final String LEFT = "\u001b[D";
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 10);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");

  @Test
  void f10OpensTheFirstMenuUnderItsTitleWithItsFirstItemSelectedAndAShadow() {
    open();
    press(F10 + DOWN + UP + UP);
    desktop.draw(screen);

    assertEquals(List.of(
        " Archivo  Ayuda         ",
        "┌───────┐               ",
        "│ Nuevo │               ",
        "│ Salir │               ",
        "└───────┘               ",
        "                        "), screen.lines().subList(0, 6));
    assertEquals("fffffffffmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("mfffffffms..............", screen.tints().get(2));
    assertEquals("mmmmmmmmms..............", screen.tints().get(3));
    assertEquals(".sssssssss..............", screen.tints().get(5));
  }

  @Test
  void theArrowsWalkTheItemsAndTheMenusAndEnterRunsTheSelectedItemAndClosesTheMenu() {
    open();
    press(F10 + DOWN + RIGHT + LEFT + DOWN + DOWN + "\r");
    desktop.draw(screen);

    assertEquals("║salir                 ║", screen.lines().get(7));
    assertEquals("                        ", screen.lines().get(1));
  }

  @Test
  void whileAMenuIsOpenTheKeysDoNotReachTheWindowsAndF10ClosesIt() {
    open();
    press(F10 + "\t" + F10 + "\r");
    desktop.draw(screen);

    assertEquals("║ok                    ║", screen.lines().get(7));
    assertEquals("                        ", screen.lines().get(1));
    assertEquals(" Archivo  Ayuda         ", screen.lines().get(0));
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void leftOnTheFirstMenuWrapsToTheLastWhoseBoxIsDrawnUnderItsTitle() {
    open();
    press(F10 + LEFT);
    desktop.draw(screen);

    assertEquals(List.of(
        " Archivo  Ayuda         ",
        "         ┌────────┐     ",
        "         │ Acerca │     ",
        "         └────────┘     ",
        "                        "), screen.lines().subList(0, 5));
    assertEquals("mmmmmmmmmfffffffmmmmmmmm", screen.tints().get(0));
    assertEquals(".........mffffffffms....", screen.tints().get(2));
  }

  @Test
  void rightOnTheLastMenuWrapsToTheFirst() {
    open();
    press(F10 + RIGHT + RIGHT);
    desktop.draw(screen);

    assertEquals("┌───────┐               ", screen.lines().get(1));
    assertEquals("fffffffffmmmmmmmmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void f10WithNoMenuBarDoesNothingAndTheKeysStillReachTheWindows() {
    Window window = toolkit.window("A");
    Button ok = toolkit.button("Ok");
    window.place(toolkit.area(0, 6, 24, 4));
    window.add(said, toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 1));
    ok.onPress(pressed -> said.text("ok"));
    desktop.open(window);

    press(F10 + "\r");
    desktop.draw(screen);

    assertEquals("║ok                    ║", screen.lines().get(7));
  }

  @Test
  void aMenuWithNoItemsOpensAsItsTitleAloneAndEnterClosesItRunningNothing() {
    MenuBar bar = toolkit.menuBar();
    bar.add(toolkit.menu("Vacio"));
    desktop.show(bar);

    press(F10);
    desktop.draw(screen);
    assertEquals("fffffffmmmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("                        ", screen.lines().get(1));

    press("\r");
    desktop.draw(screen);
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void escapeClosesTheOpenMenu() {
    open();
    press(F10 + "\u001b");
    desktop.draw(screen);

    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
    assertEquals("                        ", screen.lines().get(1));
  }

  private void open() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Archivo");
    Menu help = toolkit.menu("Ayuda");
    Window window = toolkit.window("A");
    Button ok = toolkit.button("Ok");
    file.add("Nuevo", () -> said.text("nuevo"));
    file.add("Salir", () -> said.text("salir"));
    help.add("Acerca", () -> said.text("acerca"));
    bar.add(file);
    bar.add(help);
    desktop.show(bar);
    window.place(toolkit.area(0, 6, 24, 4));
    window.add(said, toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 1));
    Button other = toolkit.button("Otro");
    window.add(other, toolkit.point(8, 1));
    ok.onPress(pressed -> said.text("ok"));
    other.onPress(pressed -> said.text("otro"));
    desktop.open(window);
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
