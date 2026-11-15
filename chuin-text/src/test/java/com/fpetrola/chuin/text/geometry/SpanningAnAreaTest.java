package com.fpetrola.chuin.text.geometry;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** An area from (2, 1), 4 across and 3 down: columns 2 to 5, rows 1 to 3. */
class SpanningAnAreaTest {
  private final Extent extent = new TextExtent(new TextArea(2, 1, 4, 3));

  @Test
  void anAreaCoversTheCellsFromItsCornerAsFarAsItGoesAndNoOthers() {
    assertEquals(true, extent.covers(new TextPoint(2, 1)));
    assertEquals(true, extent.covers(new TextPoint(5, 3)));
    assertEquals(false, extent.covers(new TextPoint(6, 3)));
    assertEquals(false, extent.covers(new TextPoint(5, 4)));
    assertEquals(false, extent.covers(new TextPoint(1, 2)));
  }

  @Test
  void thePartOfAnAreaAnotherShowsIsWhereBothAreAndNothingWhenTheyDoNotMeet() {
    Area part = extent.within(new TextArea(4, 0, 10, 2));
    Area none = extent.within(new TextArea(9, 9, 2, 2));

    assertEquals(List.of(4, 1, 2, 1), List.of(part.column(), part.row(), part.columns(), part.rows()));
    assertEquals(List.of(0, 0), List.of(none.columns(), none.rows()));
  }

  @Test
  void aCellOfTheScreenIsCountedFromTheAreasCorner() {
    Point at = extent.from(new TextPoint(5, 2));

    assertEquals(List.of(3, 1), List.of(at.column(), at.row()));
  }
}
