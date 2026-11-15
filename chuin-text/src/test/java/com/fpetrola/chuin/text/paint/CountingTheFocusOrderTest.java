package com.fpetrola.chuin.text.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CountingTheFocusOrderTest {
  @Test
  void onlyTheFocusableViewAtTheFocusedPlaceIsTheFocusedOne() {
    FocusOrder order = new TextFocusOrder(2);

    assertEquals(false, order.next());
    assertEquals(false, order.next());
    assertEquals(true, order.next());
    assertEquals(false, order.next());
  }
}
