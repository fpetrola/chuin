package com.fpetrola.tui.demo;

import com.fpetrola.tui.Button;
import com.fpetrola.tui.CheckBox;
import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.InputLine;
import com.fpetrola.tui.Label;
import com.fpetrola.tui.ListBox;
import com.fpetrola.tui.TextToolkit;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.Window;
import java.util.List;

/** Runs a window with a button on this process's console. Launch it through demo.sh; Ctrl+Q quits. */
public class Demo {
  private Demo() {
  }

  public static void main(String[] args) {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Demo");
    Button yes = toolkit.button("Sí");
    Button no = toolkit.button("No");
    CheckBox remember = toolkit.checkBox("Recordar");
    Label message = toolkit.label("Tab cambia, Enter elige");
    Label nameLabel = toolkit.label("Nombre:");
    InputLine name = toolkit.inputLine(15);
    Label colorLabel = toolkit.label("Color:");
    ListBox colors = toolkit.listBox(List.of("Rojo", "Verde", "Azul", "Amarillo", "Negro", "Blanco"), 4);

    window.place(toolkit.area(2, 1, 30, 13));
    window.add(yes, toolkit.point(7, 1));
    window.add(no, toolkit.point(15, 1));
    window.add(remember, toolkit.point(7, 3));
    window.add(message, toolkit.point(2, 4));
    window.add(nameLabel, toolkit.point(2, 5));
    window.add(name, toolkit.point(10, 5));
    window.add(colorLabel, toolkit.point(2, 7));
    window.add(colors, toolkit.point(10, 7));
    yes.onPress(pressed -> message.text("Elegiste Sí"));
    no.onPress(pressed -> message.text("Elegiste No"));
    desktop.open(help(toolkit));
    desktop.open(window);
    toolkit.console(System.in, System.out, toolkit.screen(80, 24)).run(desktop);
  }

  /** A window that lists what each key does. */
  private static Window help(Toolkit toolkit) {
    Window help = toolkit.window("Ayuda");
    help.place(toolkit.area(36, 4, 34, 9));
    List<String> keys = List.of(
        "Tab      pasa al siguiente",
        "Enter    aprieta el botón",
        "Espacio  tilda o escribe",
        "Flechas  mueven ventana/lista",
        "F6       cambia de ventana",
        "Ctrl+W   cierra la ventana",
        "Ctrl+Q   sale");
    for (int line = 0; line < keys.size(); line++) {
      help.add(toolkit.label(keys.get(line)), toolkit.point(1, line));
    }
    return help;
  }
}
