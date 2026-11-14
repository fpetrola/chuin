package com.fpetrola.tui.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class DemoTest {
  @Test
  void mainShowsTheDemoWindowAndEnterChangesItsMessage() {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream("\r".getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(shown, true, StandardCharsets.UTF_8));

      Demo.main(new String[0]);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }

    String output = shown.toString(StandardCharsets.UTF_8);
    assertTrue(output.contains(" Demo ") && output.contains("[ OK ]") && output.contains("¡Presionado!"), output);
  }
}
