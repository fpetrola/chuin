package com.fpetrola.tui.demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class DemoTest {
  private static final String F2 = "\u001b[12~";
  private static final String F10 = "\u001b[21~";

  @Test
  void typingANameAndACityThenF2SavesTheCardAndF10AndRightShowTheArchivoMenu() {
    String output = run("Ana" + "\u001bc" + "Rosario" + F2 + F10 + "\u001b[C");

    assertTrue(output.contains("Agenda (ficha 1/1)"), output);
    assertTrue(output.contains(" ≡  Archivo "), output);
    assertTrue(output.contains(" F1 Ayuda  F2 Guardar  F3 Limpiar "), output);
    assertTrue(output.contains("Guardado: Ana, Rosario, Activo"), output);
    assertTrue(output.contains("Guardar  F2"), output);
    assertTrue(output.contains("(•) Argentina"), output);
  }

  @Test
  void theDemoTakesTheRowsAndColumnsItIsGivenAndPutsTheStatusLineOnTheLastRow() {
    String output = run("", "30", "100");

    String plain = output.replaceAll("\u001b\\[[0-9;?]*[a-zA-Z]", "");

    assertTrue(output.contains("\u001b[30;1H"), "no row 30");
    assertFalse(output.contains("\u001b[31;1H"), "a row 31");
    assertTrue(plain.contains(" F1 Ayuda  F2 Guardar  F3 Limpiar " + " ".repeat(100 - 34)), "the status line is not 100 wide");
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
