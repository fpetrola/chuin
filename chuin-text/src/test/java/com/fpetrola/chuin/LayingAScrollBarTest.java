package com.fpetrola.chuin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LayingAScrollBarTest {
  private final BarLine down = new TextBarLine(new TextPoint(9, 2), new TextPoint(0, 1));
  private final BarLine across = new TextBarLine(new TextPoint(3, 7), new TextPoint(1, 0));

  @Test
  void aBarDownHoldsTheCellsOfItsColumnAndCountsThemByRow() {
    assertEquals(true, down.holds(new TextPoint(9, 5)));
    assertEquals(false, down.holds(new TextPoint(8, 2)));
    assertEquals(3, down.along(new TextPoint(9, 5)));
    assertEquals(9, down.cell(4).column());
    assertEquals(6, down.cell(4).row());
  }

  @Test
  void aBarAcrossHoldsTheCellsOfItsRowAndCountsThemByColumn() {
    assertEquals(true, across.holds(new TextPoint(10, 7)));
    assertEquals(false, across.holds(new TextPoint(3, 8)));
    assertEquals(7, across.along(new TextPoint(10, 7)));
    assertEquals(5, across.cell(2).column());
    assertEquals(7, across.cell(2).row());
  }
}
