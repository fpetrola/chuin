package com.fpetrola.chuin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class KeepingACursorAmongPlacesTest {
  @Test
  void aCursorStartsAtTheFirstPlaceAndStopsAtEitherEnd() {
    Cursor cursor = new TextCursor(3);

    assertEquals(0, cursor.at());
    cursor.step(5);
    assertEquals(2, cursor.at());
    cursor.step(-1);
    assertEquals(1, cursor.at());
    cursor.step(-9);
    assertEquals(0, cursor.at());
    cursor.moveTo(2);
    assertEquals(2, cursor.at());
    cursor.moveTo(7);
    assertEquals(2, cursor.at());
  }

  @Test
  void withNoPlacesACursorStaysAtTheFirst() {
    Cursor cursor = new TextCursor(0);

    cursor.moveTo(4);

    assertEquals(0, cursor.at());
  }
}
