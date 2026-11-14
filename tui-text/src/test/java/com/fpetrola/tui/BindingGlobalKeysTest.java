package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** A key bound on the desktop runs its command whatever view has the focus, and does not reach the windows. */
class BindingGlobalKeysTest {
  private static final String F3 = "\u001bOR";
  private static final String F5 = "\u001b[15~";
  private static final String F10 = "\u001b[21~";
  private final Toolkit toolkit = new TextToolkit();
  private final Desktop desktop = toolkit.desktop();
  private final Keys keys = toolkit.keys();
  private final Label said = toolkit.label("");

  @Test
  void aBoundFunctionKeyRunsItsCommandWithTheFocusAnywhere() {
    open();
    desktop.bind(keys.function(3), () -> said.text("laden"));
    press("\t" + F3);

    assertEquals("laden", said.text());
  }

  @Test
  void aFunctionKeyNothingIsBoundToStillReachesTheWindowsAsBefore() {
    open();
    desktop.bind(keys.function(3), () -> said.text("laden"));
    press(F5 + "\r");

    assertEquals("ok", said.text());
  }

  @Test
  void theSameFunctionKeyIsTheSameWhicheverWayTheTerminalSendsIt() {
    open();
    desktop.bind(keys.function(1), () -> said.text("hilfe"));
    press("\u001b[11~");

    assertEquals("hilfe", said.text());
  }

  @Test
  void withAMenuOpenABoundKeyRunsItsCommandAndClosesTheMenu() {
    open();
    showMenu();
    desktop.bind(keys.function(3), () -> said.text("laden"));
    press(F10 + F3);
    Screen screen = toolkit.screen(20, 6);
    desktop.draw(screen);

    assertEquals("laden", said.text());
    assertEquals("wwwwwwwwwwwwwwwwwwww", screen.tints().get(0));
  }

  @Test
  void aBindingWinsOverWhatTheKeyDidBeforeSoBindingF10KeepsTheMenuClosed() {
    open();
    showMenu();
    desktop.bind(keys.function(10), () -> said.text("eigenes"));
    press(F10);
    Screen screen = toolkit.screen(20, 6);
    desktop.draw(screen);

    assertEquals("eigenes", said.text());
    assertEquals("wwwwwwwwwwwwwwwwwwww", screen.tints().get(0));
  }

  @Test
  void bindingAKeyAgainReplacesItsCommand() {
    open();
    desktop.bind(keys.function(3), () -> said.text("laden"));
    desktop.bind(keys.function(3), () -> said.text("öffnen"));
    press(F3);

    assertEquals("öffnen", said.text());
  }

  private void showMenu() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Datei");
    file.add("Neu", () -> said.text("neu"));
    bar.add(file);
    desktop.show(bar);
  }

  private void open() {
    Window window = toolkit.window("W");
    Button ok = toolkit.button("Ok");
    window.place(toolkit.area(0, 1, 20, 5));
    window.add(said, toolkit.point(0, 0));
    window.add(ok, toolkit.point(0, 2));
    window.add(toolkit.button("Otro"), toolkit.point(8, 2));
    ok.onPress(pressed -> said.text("ok"));
    desktop.open(window);
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
