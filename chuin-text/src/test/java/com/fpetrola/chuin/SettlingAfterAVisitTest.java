package com.fpetrola.chuin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SettlingAfterAVisitTest {
  @Test
  void settlingDoesWhatWasNotedLastAndNothingBefore() {
    StringBuilder done = new StringBuilder();
    Settle pending = new PendingAction(() -> done.append("otherwise"));

    pending.note(() -> done.append("first"));
    pending.note(() -> done.append("last"));
    assertEquals("", done.toString());

    pending.settle();
    assertEquals("last", done.toString());
  }
}
