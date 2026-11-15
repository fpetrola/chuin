package com.fpetrola.tui;

import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** "~A~rchivo" placed at column 3 covers columns 3 to 11 of the top row; its menu opens under it from row 2. */
class PlacingATitleOnTheBarTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Menu file = toolkit.menu("~A~rchivo");
  private final PlacedTitle title = new TextPlacedTitle(file, 3);

  PlacingATitleOnTheBarTest() {
    file.add("~G~uardar", () -> { });
    file.separator();
    file.add("~S~alir", () -> { });
  }

  @Test
  void aTitleCoversItsCaptionAndASpaceOnBothSidesOnTheTopRowOnly() {
    assertEquals(file, title.menu());
    assertFalse(title.covers(new TextPoint(2, 0)));
    assertTrue(title.covers(new TextPoint(3, 0)));
    assertTrue(title.covers(new TextPoint(11, 0)));
    assertFalse(title.covers(new TextPoint(12, 0)));
    assertFalse(title.covers(new TextPoint(4, 1)));
  }

  @Test
  void aTitleAnswersTheLetterMarkedInItsCaption() {
    assertTrue(title.answers('a'));
    assertFalse(title.answers('r'));
  }

  @Test
  void theItemAtACellIsTheOneDrawnThereAndNothingOnASeparatorOrOffTheMenu() {
    assertEquals("~G~uardar", title.itemAt(new TextPoint(4, 2)).map(MenuItem::text).orElseThrow());
    assertEquals(Optional.empty(), title.itemAt(new TextPoint(4, 3)));
    assertEquals("~S~alir", title.itemAt(new TextPoint(4, 4)).map(MenuItem::text).orElseThrow());
    assertEquals(Optional.empty(), title.itemAt(new TextPoint(4, 5)));
    assertEquals(Optional.empty(), title.itemAt(new TextPoint(2, 2)));
    assertEquals(List.of("~G~uardar", "~S~alir"), title.items().stream().map(MenuItem::text).toList());
  }
}
