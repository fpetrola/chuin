package com.fpetrola.chuin;

import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.screen.Terminal;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** The focused input line shows the cursor right after its text; with the focus anywhere else there is no cursor. */
class SeeingTheCursorTest {
  private static final String ESC = "\u001b[";
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(20, 4);
  private final Desktop desktop = toolkit.desktop();

  @Test
  void theFocusedInputLineShowsTheCursorRightAfterItsText() {
    open();
    toolkit.keyboard(new ByteArrayInputStream("\tAna".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals(Optional.of(toolkit.point(5, 2)), screen.cursor());
  }

  @Test
  void withTheFocusOnAnythingButAnInputLineThereIsNoCursor() {
    open();
    toolkit.keyboard(new ByteArrayInputStream("\tAna\t".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals(Optional.empty(), screen.cursor());
  }

  @Test
  void aCursorShownBeforeIsGoneWhenTheFocusLeavesTheInputLineAndTheScreenIsDrawnAgain() {
    open();
    toolkit.keyboard(new ByteArrayInputStream("\tAna".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);
    assertEquals(Optional.of(toolkit.point(5, 2)), screen.cursor());

    desktop.type(toolkit.tabKey());
    desktop.draw(screen);

    assertEquals(Optional.empty(), screen.cursor());
  }

  @Test
  void withAMenuOpenThereIsNoCursorBecauseTheKeysGoToTheMenu() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Archivo");
    file.add("Salir", () -> { });
    bar.add(file);
    desktop.show(bar);
    open();
    toolkit.keyboard(new ByteArrayInputStream("\tAna\u001b[21~".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals(Optional.empty(), screen.cursor());
  }

  @Test
  void aFullInputLineKeepsTheCursorOnItsLastCellSoItNeverFallsOnTheFrame() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 6, 3));
    window.add(toolkit.inputLine(4), toolkit.point(0, 0));
    desktop.open(window);
    toolkit.keyboard(new ByteArrayInputStream("Anas".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals(Optional.of(toolkit.point(4, 1)), screen.cursor());
  }

  @Test
  void theTerminalPutsTheCursorWhereTheScreenSaysAndShowsItOrHidesIt() {
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    Terminal terminal = toolkit.terminal(shown);
    Screen small = toolkit.screen(2, 1);

    small.showCursorAt(toolkit.point(1, 0));
    terminal.show(small);
    String withCursor = shown.toString(StandardCharsets.UTF_8);
    shown.reset();
    small.hideCursor();
    terminal.show(small);
    String withoutCursor = shown.toString(StandardCharsets.UTF_8);

    assertEquals(ESC + "1;2H" + ESC + "?25h", withCursor.substring(withCursor.indexOf(ESC + "0m") + (ESC + "0m").length()));
    assertEquals(ESC + "?25l", withoutCursor.substring(withoutCursor.indexOf(ESC + "0m") + (ESC + "0m").length()));
  }

  private void open() {
    Window window = toolkit.window("A");
    window.place(toolkit.area(0, 0, 20, 4));
    window.add(toolkit.button("Ok"), toolkit.point(0, 0));
    window.add(toolkit.inputLine(8), toolkit.point(1, 1));
    desktop.open(window);
  }
}
