package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.view.Button;
import com.fpetrola.tui.view.InputLine;
import com.fpetrola.tui.view.Label;
import com.fpetrola.tui.view.ListBox;
import com.fpetrola.tui.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ReadingWhatWasEnteredTest {
  @Test
  void aButtonCanReadTheNameTypedAndTheColorSelected() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(30, 9);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Saludo");
    InputLine name = toolkit.inputLine(10);
    ListBox colors = toolkit.listBox(List.of("Rojo", "Verde", "Azul"), 3);
    Button greet = toolkit.button("Ok");
    Label greeting = toolkit.label("");
    String down = "\u001b[B";

    window.place(toolkit.area(0, 0, 30, 8));
    window.add(name, toolkit.point(0, 0));
    window.add(colors, toolkit.point(0, 1));
    window.add(greet, toolkit.point(0, 4));
    window.add(greeting, toolkit.point(0, 5));
    greet.onPress(pressed -> greeting.text("Hola " + name.text() + ", elegiste " + colors.selected().orElse("ninguno")));
    desktop.open(window);
    toolkit.keyboard(new ByteArrayInputStream(("Ana\t" + down + "\t\r").getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
    desktop.draw(screen);

    assertEquals("║Hola Ana, elegiste Verde    ║", screen.lines().get(6));
  }

  @Test
  void theTextReadIsWhatIsLeftAfterBackspaceAndAnEmptyListHasNoSelection() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    InputLine name = toolkit.inputLine(10);
    ListBox nothing = toolkit.listBox(List.of(), 3);

    window.add(name, toolkit.point(0, 0));
    window.add(nothing, toolkit.point(0, 1));
    desktop.open(window);
    toolkit.keyboard(new ByteArrayInputStream("Anax\u007f".getBytes(StandardCharsets.UTF_8))).typeInto(desktop);

    assertEquals("Ana", name.text());
    assertEquals(Optional.empty(), nothing.selected());
  }
}
