package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** The status line runs across the bottom row: each entry shows its key highlighted, runs on that key and on a click. */
class UsingTheStatusLineTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 5);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");
  private final Window window = toolkit.window("W");

  @Test
  void theStatusLineShowsItsEntriesAcrossTheBottomRowWithTheirKeysHighlighted() {
    open();
    desktop.draw(screen);

    assertEquals(" Esc Raus  F3 Laden    ", screen.lines().get(4).substring(0, 23));
    assertEquals("mhhhmmmmmmmhhmmmmmmmmmmm", screen.tints().get(4));
  }

  @Test
  void theKeyOfAnEntryRunsItWhateverHasTheFocus() {
    open();
    press("\u001bOR");

    assertEquals("laden", said.text());
  }

  @Test
  void aClickOnAnEntryRunsItAndDoesNotReachTheWindowUnderIt() {
    open();
    desktop.draw(screen);
    press("\u001b[<0;3;5M");

    assertEquals("raus", said.text());
  }

  @Test
  void escapeWithAMenuOpenClosesTheMenuAndDoesNotRunTheEntryBoundToEscape() {
    open();
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Datei");
    file.add("Neu", () -> said.text("neu"));
    bar.add(file);
    desktop.show(bar);
    press("\u001b[21~\u001b");
    desktop.draw(screen);

    assertEquals("", said.text());
    assertEquals("mmmmmmmmmmmmmmmmmmmmmmmm", screen.tints().get(0));
  }

  @Test
  void aClickOnTheStatusRowAwayFromTheEntriesDoesNotReachTheWindowUnderIt() {
    open();
    Button under = toolkit.button("X");
    under.onPress(pressed -> said.text("x"));
    window.place(toolkit.area(0, 0, 24, 7));
    window.add(under, toolkit.point(19, 3));
    desktop.draw(screen);
    press("\u001b[<0;23;5M");

    assertEquals("", said.text());
  }

  @Test
  void aStatusLineIsVisitedThenEachEntryWithItsKey() {
    StatusLine status = toolkit.statusLine();
    status.add("~F3~ Laden", toolkit.functionKey(3), () -> said.text("laden"));
    List<String> seen = new ArrayList<>();

    status.accept(new StatusVisitor() {
      @Override
      public void visitStatusLine(StatusLine line) {
        seen.add("line");
      }

      @Override
      public void visitStatusItem(StatusItem item) {
        seen.add(item.text());
        item.key().accept(new KeyVisitor() {
          @Override
          public void function(int number) {
            seen.add("F" + number);
          }
        });
        item.run();
      }
    });

    assertEquals(List.of("line", "~F3~ Laden", "F3"), seen);
    assertEquals("laden", said.text());
  }

  /** A status line with ~Esc~ Raus and ~F3~ Laden over a window that reaches the bottom row. */
  private void open() {
    StatusLine status = toolkit.statusLine();
    status.add("~Esc~ Raus", toolkit.escapeKey(), () -> said.text("raus"));
    status.add("~F3~ Laden", toolkit.functionKey(3), () -> said.text("laden"));
    desktop.show(status);
    window.place(toolkit.area(0, 0, 24, 5));
    window.add(said, toolkit.point(0, 0));
    Button ok = toolkit.button("Ok");
    ok.onPress(pressed -> said.text("ok"));
    window.add(ok, toolkit.point(0, 2));
    desktop.open(window);
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
