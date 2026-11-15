package com.fpetrola.chuin;

import com.fpetrola.chuin.text.TextToolkit;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ChangingALabelTest {
  @Test
  void pressingTheButtonChangesTheTextTheLabelShows() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 5);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Button ok = toolkit.button("OK");
    Label message = toolkit.label("");

    window.place(toolkit.area(0, 0, 20, 5));
    window.add(ok, toolkit.point(1, 0));
    window.add(message, toolkit.point(1, 2));
    ok.onPress(pressed -> message.text("¡Presionado!"));
    desktop.open(window);
    desktop.type(toolkit.enterKey());
    desktop.draw(screen);

    assertEquals(List.of(
        "╔═══════ A ════════╗",
        "║   OK  ▄          ║",
        "║  ▀▀▀▀▀▀          ║",
        "║ ¡Presionado!     ║",
        "╚══════════════════╝"), screen.lines());
  }
}
