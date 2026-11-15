package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** A scroll bar has no place of its own: it shows where what it is beside is, and its moves scroll that. */
class ScrollingWithABarTest {
  private final TextListBox numbers = new TextListBox(List.of("uno", "dos", "tres", "cuatro", "cinco", "seis"), 4);
  private final Scrollable list = numbers;
  private final ScrollBar bar = new TextScrollBar(list, 4);

  @Test
  void aListTellsTheBarWhereItsSelectionIsHowFarItGoesAndWhetherItHoldsMoreThanItShows() {
    numbers.next();

    assertEquals(1, list.position());
    assertEquals(5, list.most());
    assertEquals(true, list.overflows());
    assertEquals(false, new TextListBox(List.of("uno"), 3).overflows());
  }

  @Test
  void theMovesOfTheBarScrollWhatItIsBeside() {
    bar.next();
    bar.next();
    assertEquals("tres", numbers.selected().orElseThrow());

    bar.previous();
    assertEquals("dos", numbers.selected().orElseThrow());

    bar.pageDown();
    assertEquals("cinco", numbers.selected().orElseThrow());

    bar.pageUp();
    assertEquals("dos", numbers.selected().orElseThrow());
  }

  @Test
  void theBarIsWalkedAsTheArrowsAndATrackWithTheThumbWhereWhatItIsBesideIs() {
    StringBuilder cells = new StringBuilder();
    ViewVisitor drawn = new ViewVisitor() {
      @Override
      public void visitScrollUp(ScrollBar visited) {
        cells.append("▲");
      }

      @Override
      public void visitScrollTrack(ScrollBar visited) {
        cells.append("░");
      }

      @Override
      public void visitScrollThumb(ScrollBar visited) {
        cells.append("■");
      }

      @Override
      public void visitScrollDown(ScrollBar visited) {
        cells.append("▼");
      }
    };

    bar.accept(drawn);
    for (int step = 0; step < 5; step++) {
      numbers.next();
    }
    bar.accept(drawn);

    assertEquals("▲■░▼" + "▲░■▼", cells.toString());
  }

  /** Six items in four rows: the track is cells 1 and 2, so the thumb on the last of them goes to the last item. */
  @Test
  void puttingTheThumbOnACellMovesWhatTheBarIsBesideAsFarAlongAndTheEndsHoldIt() {
    bar.thumbTo(2);
    assertEquals("seis", numbers.selected().orElseThrow());

    bar.thumbTo(0);
    assertEquals("uno", numbers.selected().orElseThrow());

    list.moveTo(3);
    assertEquals("cuatro", numbers.selected().orElseThrow());

    list.moveTo(9);
    assertEquals("seis", numbers.selected().orElseThrow());
  }
}
