package com.fpetrola.chuin;

import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PuttingEntriesInARowTest {
  @Test
  void entriesGoLeftToRightEachWithABlankOnEachSideItsTintAndItsMarkedLettersHighlighted() {
    Screen screen = new TextToolkit().screen(16, 1);
    EntryRow row = new TextEntryRow(screen, 0);

    row.put(new TextCaption("~D~atei"), TintRole.WINDOW, TintRole.LETTER);
    row.put(new TextCaption("~F3~ X"), TintRole.FOCUS, TintRole.FOCUS_LETTER);

    assertEquals(" Datei  F3 X    ", screen.lines().getFirst());
    assertEquals("whwwwwwfkkfff...", screen.tints().getFirst());
  }

  @Test
  void eachEntryIsPlacedWhereTheOneBeforeEndsAndCoversItsCaptionAndABlankOnEachSide() {
    EntryPlacer placer = new TextEntryPlacer();

    PlacedEntry file = placer.place(new TextCaption("~D~atei"));
    PlacedEntry load = placer.place(new TextCaption("~F3~ X"));

    assertEquals(0, file.start());
    assertEquals(7, load.start());
    assertEquals(true, file.covers(6));
    assertEquals(false, file.covers(7));
    assertEquals(true, load.covers(12));
    assertEquals(false, load.covers(13));
  }
}
