package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Terminals report the mouse as ESC [ < button ; column ; row, counted from 1, ending in M when pressed and m when released. */
class ClickingWithTheMouseTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(22, 6);
  private final Desktop desktop = toolkit.desktop();
  private final Window window = toolkit.window("M");
  private final Label said = toolkit.label("");

  @Test
  void aLeftClickOnAButtonFocusesAndPressesIt() {
    open();
    click(0, 11, 1);
    desktop.draw(screen);

    assertEquals("║  Ok  ▄   No  ▄     ║", screen.lines().get(1));
    assertEquals("wbbbbbbwwffffffwwwwwww", screen.tints().get(1));
    assertEquals("║no                  ║", screen.lines().get(3));
  }

  @Test
  void aLeftClickOnACheckBoxFocusesAndTogglesIt() {
    open();
    click(0, 3, 2);
    desktop.draw(screen);

    assertEquals("║  Ok  ▄   No  ▄     ║", screen.lines().get(1));
    assertEquals("wbbbbbbwwbbbbbbwwwwwww", screen.tints().get(1));
    assertEquals("║[X] C▀▀  ▀▀▀▀▀▀     ║", screen.lines().get(2));
    assertEquals("wfffffwwwwwwwwwwwwwwww", screen.tints().get(2));
  }

  @Test
  void otherButtonsReleasesAndClicksOnNothingDoNothing() {
    open();
    int right = 2;
    click(right, 11, 1);
    click(0, 18, 3);
    type("\u001b[<0;12;2m");
    desktop.draw(screen);

    assertEquals("║  Ok  ▄   No  ▄     ║", screen.lines().get(1));
    assertEquals("wffffffwwbbbbbbwwwwwww", screen.tints().get(1));
    assertEquals("║                    ║", screen.lines().get(3));
  }

  @Test
  void aLeftClickOnAnInputLineOnlyFocusesItAndTypesNothing() {
    InputLine name = toolkit.inputLine(6);
    window.place(toolkit.area(0, 0, 22, 5));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(name, toolkit.point(0, 1));
    desktop.open(window);
    desktop.draw(screen);

    click(0, 2, 2);
    type("Ana");
    desktop.draw(screen);
    click(0, 2, 2);
    desktop.draw(screen);

    assertEquals("Ana", name.text());
    assertEquals("║  Ok  ▄             ║", screen.lines().get(1));
    assertEquals("bbbbbb", screen.tints().get(1).substring(1, 7));
  }

  @Test
  void aLeftClickOnAWindowBelowRaisesItWithItsOwnFocusAndPressesNothing() {
    Window below = toolkit.window("B");
    Button no = toolkit.button("No");
    Button ni = toolkit.button("Ni");
    Button si = toolkit.button("Si");
    below.place(toolkit.area(0, 0, 9, 4));
    below.add(no, toolkit.point(0, 0));
    below.add(ni, toolkit.point(0, 1));
    window.place(toolkit.area(10, 0, 9, 4));
    window.add(si, toolkit.point(0, 0));
    window.add(said, toolkit.point(0, 1));
    no.onPress(pressed -> said.text("no"));
    ni.onPress(pressed -> said.text("ni"));
    si.onPress(pressed -> said.text("si"));
    desktop.open(below);
    type("\t");
    desktop.open(window);
    desktop.draw(screen);

    click(0, 3, 1);
    desktop.draw(screen);

    assertEquals("║  No  ▄║ │  Si  ▄│   ", screen.lines().get(1));
    assertEquals("║  Ni  ▄║ │ ▀▀▀▀▀▀│   ", screen.lines().get(2));
    assertEquals("ffffff", screen.tints().get(2).substring(1, 7));
    assertEquals("bbbbbb", screen.tints().get(1).substring(11, 17));
  }

  @Test
  void aLeftClickOnTheBareDesktopChangesNothing() {
    open();
    click(0, 0, 5);
    desktop.draw(screen);

    assertEquals("║  Ok  ▄   No  ▄     ║", screen.lines().get(1));
    assertEquals("wffffffwwbbbbbbwwwwwww", screen.tints().get(1));
    assertEquals("║                    ║", screen.lines().get(3));
  }


  @Test
  void aLeftClickOnARowOfAListOrOnAnOptionFocusesAndChoosesIt() {
    ListBox colors = toolkit.listBox(List.of("Rojo", "Verde", "Azul"), 3);
    window.place(toolkit.area(0, 0, 22, 5));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(colors, toolkit.point(0, 1));
    window.add(toolkit.optionGroup(List.of("Si", "No")), toolkit.point(8, 1));
    desktop.open(window);
    desktop.draw(screen);

    click(0, 1, 3);
    desktop.draw(screen);
    assertEquals(Optional.of("Verde"), colors.selected());
    assertEquals("wbbbbbbwwwwwwwwwwwwwww", screen.tints().get(1));
    assertEquals("wwwwwwwwwccccccwwwwwww", screen.tints().get(2));
    assertEquals("wfffffwwwccccccwwwwwww", screen.tints().get(3));

    click(0, 9, 3);
    desktop.draw(screen);
    assertEquals("║Rojo    ( ) Si      ■", screen.lines().get(2));
    assertEquals("║Verde   (•) No      ▼", screen.lines().get(3));
    assertEquals("wwwwwwwwwffffffwwwwwww", screen.tints().get(3));
  }

  @Test
  void aLeftClickBelowTheLastItemOfAListOnlyFocusesIt() {
    ListBox one = toolkit.listBox(List.of("Solo"), 3);
    window.place(toolkit.area(0, 0, 22, 5));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(one, toolkit.point(0, 1));
    desktop.open(window);
    desktop.draw(screen);

    click(0, 1, 3);
    desktop.draw(screen);

    assertEquals(Optional.of("Solo"), one.selected());
    assertEquals("wffffwwwwwwwwwwwwwwwww", screen.tints().get(2));
  }


  private void open() {
    Button ok = toolkit.button("Ok");
    Button no = toolkit.button("No");
    window.place(toolkit.area(0, 0, 22, 5));
    window.add(ok, toolkit.point(0, 0));
    window.add(no, toolkit.point(8, 0));
    window.add(toolkit.checkBox("C"), toolkit.point(0, 1));
    window.add(said, toolkit.point(0, 2));
    ok.onPress(pressed -> said.text("ok"));
    no.onPress(pressed -> said.text("no"));
    desktop.open(window);
    desktop.draw(screen);
  }

  /** Presses and releases a mouse button on a screen cell, counted from 0. */
  private void click(int button, int column, int row) {
    String at = button + ";" + (column + 1) + ";" + (row + 1);
    type("\u001b[<" + at + "M" + "\u001b[<" + at + "m");
  }

  private void type(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
