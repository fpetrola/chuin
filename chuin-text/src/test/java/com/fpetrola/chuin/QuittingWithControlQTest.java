package com.fpetrola.chuin;

import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.ButtonListener;
import com.fpetrola.chuin.view.Window;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class QuittingWithControlQTest {
  private static final String CONTROL_Q = "\u0011";

  @Test
  void controlQEndsTheRunSoKeysTypedAfterItAreNotRead() {
    Toolkit toolkit = new TextToolkit();
    ByteArrayInputStream typed = new ByteArrayInputStream((CONTROL_Q + "\r").getBytes(StandardCharsets.UTF_8));
    Console console = toolkit.console(typed, new ByteArrayOutputStream(), toolkit.screen(10, 3));
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);

    window.place(toolkit.area(0, 0, 10, 3));
    ok.onPress(listener);
    window.add(ok, toolkit.point(0, 0));
    desktop.open(window);
    console.run(desktop);

    verifyNoInteractions(listener);
  }
}
