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
  void tabEnterChoosesNoTabSpaceChecksRememberAndTabTypesANameInTheDemo() {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream shown = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream("\t\r\t \tAna\t\u001b[B\u001b[B\t\r\u001b[21~\u001b[C\r".getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(shown, true, StandardCharsets.UTF_8));

      Demo.main(new String[0]);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }

    String output = shown.toString(StandardCharsets.UTF_8).replaceAll("\u001b\\[[0-9;]*m", "");
    assertTrue(output.contains(" Demo ") && output.contains("Elegiste No") && output.contains("[X] Recordar") && output.contains("Nombre: Ana            ") && output.contains("Color:  Rojo     ▲") && output.contains("Verde    ■") && output.contains(" Archivo  Ayuda ") && output.contains("Una TUI hecha con cerco") && output.contains(" Ayuda ") && output.contains("Hola Ana, elegiste Azul") && output.contains("F6       cambia de ventana"), output);
  }
}
