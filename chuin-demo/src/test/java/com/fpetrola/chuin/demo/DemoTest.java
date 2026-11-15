package com.fpetrola.chuin.demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class DemoTest {
  private static final String F10 = "\u001b[21~";

  @Test
  void withNoNameTheAddressBookRunsAndF10ShowsTheFileMenuWithTheKeysOfItsItems() {
    String output = run(F10 + "\u001b[C");

    assertTrue(output.contains("Address database  (record 7 of 13)"), output);
    assertTrue(output.contains(" ≡  File  Edit  Search  Options "), output);
    assertTrue(output.contains(" F1 Help  F2 Save  F4 New  F7 Find "), output);
    assertTrue(output.contains("│ New record        F4 │"), output);
    assertTrue(output.contains("(•) Ms."), output);
  }

  @Test
  void anExampleIsRunByItsNameAndAnUnknownNameListsTheNames() {
    assertTrue(run("", "hello").contains("Hello, world!"));
    assertTrue(run("", "nothing").contains("Examples: [address-book, inventory, hello, form, lists, menus, scrolling]"));
  }

  @Test
  void theDemoTakesTheRowsAndColumnsItIsGivenAndPutsTheStatusLineOnTheLastRow() {
    String output = run("", "30", "100");

    String plain = output.replaceAll("\u001b\\[[0-9;?]*[a-zA-Z]", "");

    assertTrue(output.contains("\u001b[30;1H"), "no row 30");
    assertFalse(output.contains("\u001b[31;1H"), "a row 31");
    assertTrue(plain.contains(" F1 Help  F2 Save  F4 New  F7 Find " + " ".repeat(100 - 35)), "the status line is not 100 wide");
  }

  /** Runs the demo on these keys and returns what it showed, without the colour codes. */
  private static String run(String typed) {
    return run(typed, new String[0]).replaceAll("\u001b\\[[0-9;?]*[a-zA-Z]", "");
  }

  /** Runs the demo with these arguments on these keys and returns all it wrote, colour codes included. */
  private static String run(String typed, String... args) {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(shown, true, StandardCharsets.UTF_8));
      Demo.main(args);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
    return shown.toString(StandardCharsets.UTF_8);
  }
}
