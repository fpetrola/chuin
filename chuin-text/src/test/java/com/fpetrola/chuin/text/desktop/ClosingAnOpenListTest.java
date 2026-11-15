package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.Toolkit;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.text.pointer.Settle;
import com.fpetrola.chuin.text.pointer.PendingAction;
import com.fpetrola.chuin.view.DropDown;

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
    Settle settle = new PendingAction(() -> ran.append("otherwise"));

    answer.accept(new CloseOpenList(settle));
    settle.settle();

    assertEquals("", ran.toString());
    assertEquals(Optional.of("Ja"), answer.chosen());
  }

  @Test
  void settlingWhenNoListWasOpenRunsWhatElseWasAsked() {
    DropDown answer = toolkit.dropDown(List.of("Ja", "Nein"));
    Settle settle = new PendingAction(() -> ran.append("otherwise"));

    answer.accept(new CloseOpenList(settle));
    settle.settle();

    assertEquals("otherwise", ran.toString());
  }
}
