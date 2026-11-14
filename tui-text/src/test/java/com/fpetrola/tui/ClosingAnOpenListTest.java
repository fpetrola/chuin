package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Closing an open list settles once the visit is over: what else was asked runs only if no list was closed. */
class ClosingAnOpenListTest {
  private final Toolkit toolkit = new TextToolkit();
  private final StringBuilder ran = new StringBuilder();

  @Test
  void settlingAfterClosingAnOpenListRunsNothingElse() {
    DropDown answer = toolkit.dropDown(List.of("Ja", "Nein"));
    answer.down();
    answer.down();
    CloseOpenList close = new CloseOpenList(() -> ran.append("otherwise"));
    Settle settle = close;

    answer.accept(close);
    settle.settle();

    assertEquals("", ran.toString());
    assertEquals(Optional.of("Ja"), answer.chosen());
  }

  @Test
  void settlingWhenNoListWasOpenRunsWhatElseWasAsked() {
    DropDown answer = toolkit.dropDown(List.of("Ja", "Nein"));
    CloseOpenList close = new CloseOpenList(() -> ran.append("otherwise"));

    answer.accept(close);
    close.settle();

    assertEquals("otherwise", ran.toString());
  }
}
