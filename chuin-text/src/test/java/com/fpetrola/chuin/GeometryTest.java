package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class GeometryTest {
  private final Toolkit toolkit = new TextToolkit();

  @Test
  void aPointTellsItsColumnAndRow() {
    Point point = toolkit.point(3, 7);

    assertEquals(List.of(3, 7), List.of(point.column(), point.row()));
  }

  @Test
  void anAreaTellsWhereItStartsAndHowBigItIs() {
    Area area = toolkit.area(2, 4, 20, 5);

    assertEquals(List.of(2, 4, 20, 5), List.of(area.column(), area.row(), area.columns(), area.rows()));
  }
}
