package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PuttingEntriesInARowTest {
  @Test
  void entriesGoLeftToRightEachWithABlankOnEachSideItsTintAndItsMarkedLettersHighlighted() {
    Screen screen = new TextToolkit().screen(16, 1);
    EntryRow row = new TextEntryRow(screen, 0);

    row.put(new TextCaption("~D~atei"), 'w', 'h');
    int second = row.next();
    row.put(new TextCaption("~F3~ X"), 'f', 'k');

    assertEquals(7, second);
    assertEquals(13, row.next());
    assertEquals(" Datei  F3 X    ", screen.lines().getFirst());
    assertEquals("whwwwwwfkkfff...", screen.tints().getFirst());
  }
}
