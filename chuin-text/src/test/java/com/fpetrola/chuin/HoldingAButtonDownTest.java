package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ViewVisitor;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * As in Turbo Vision, pressing the mouse on a button holds it down: it is drawn a cell to the right, without its shadow.
 * Releasing over it presses it; releasing away lets it go without pressing.
 */
class HoldingAButtonDownTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(14, 6);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");

  HoldingAButtonDownTest() {
    Window window = toolkit.window("W");
    Button ok = toolkit.button("Ok");
    window.place(toolkit.area(0, 0, 14, 5));
    window.add(ok, toolkit.point(0, 0));
    window.add(said, toolkit.point(0, 2));
    ok.onPress(pressed -> said.text("ok"));
    desktop.open(window);
    desktop.draw(screen);
  }

  @Test
  void aButtonHeldDownIsDrawnACellToTheRightWithoutItsShadowAndIsNotPressedYet() {
    mouse('M', 3, 1);
    desktop.draw(screen);

    assertEquals(List.of(
        "║   Ok       ║",
        "║            ║"), screen.lines().subList(1, 3));
    assertEquals("", said.text());
  }

  @Test
  void releasingOverTheButtonPressesItAndItComesBackUp() {
    mouse('M', 3, 1);
    mouse('m', 5, 1);
    desktop.draw(screen);

    assertEquals("ok", said.text());
    assertEquals(List.of(
        "║  Ok  ▄     ║",
        "║ ▀▀▀▀▀▀     ║"), screen.lines().subList(1, 3));
  }

  @Test
  void releasingAwayFromTheButtonLetsItGoWithoutPressingIt() {
    mouse('M', 3, 1);
    mouse('m', 10, 3);
    desktop.draw(screen);

    assertEquals("", said.text());
    assertEquals("║  Ok  ▄     ║", screen.lines().get(1));
  }

  /** As in Turbo Vision, a release is over the button by where the button is, not by where it is drawn down. */
  @Test
  void releasingOnTheButtonsFirstCellPressesItEvenThoughItIsDrawnACellToTheRight() {
    mouse('M', 3, 1);
    mouse('m', 1, 1);

    assertEquals("ok", said.text());
  }

  @Test
  void aHeldButtonIsVisitedAsHeldUntilItIsLetGo() {
    Button button = toolkit.button("Ok");
    StringBuilder met = new StringBuilder();
    ViewVisitor visitor = new ViewVisitor() {
      @Override
      public void visitButton(Button visited) {
        met.append("button ");
      }

      @Override
      public void visitHeldButton(Button visited) {
        met.append("held ");
      }
    };

    button.hold();
    button.accept(visitor);
    button.letGo();
    button.accept(visitor);

    assertEquals("button held button ", met.toString());
  }

  /** A press (M) or a release (m) of the left button on a cell counted from 0. */
  private void mouse(char kind, int column, int row) {
    String typed = "\u001b[<0;" + (column + 1) + ";" + (row + 1) + kind;
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
