package com.fpetrola.chuin.text.input;

import com.fpetrola.chuin.text.TextToolkit;
import com.fpetrola.chuin.Toolkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/** The name of a key, which the rest of chuin-text makes for itself from outside this package. */
class ReadingKeysTest {
  private final Toolkit toolkit = new TextToolkit();

  @Test
  void aKeyIsNamedByWhatItIsSoTheSameKeyMadeTwiceHasTheSameName() {
    StringBuilder tab = new StringBuilder();
    StringBuilder again = new StringBuilder();
    StringBuilder f3 = new StringBuilder();

    toolkit.tabKey().accept(new KeyName(tab));
    toolkit.tabKey().accept(new KeyName(again));
    toolkit.functionKey(3).accept(new KeyName(f3));

    assertEquals("tab", tab.toString());
    assertEquals(tab.toString(), again.toString());
    assertNotEquals(tab.toString(), f3.toString());
  }
}
