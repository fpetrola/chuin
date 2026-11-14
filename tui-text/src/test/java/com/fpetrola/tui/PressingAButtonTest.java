package com.fpetrola.tui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class PressingAButtonTest {
  @Test
  void enterOnFocusedButtonNotifiesItsListener() {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Saludo");
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);
    Keys keys = toolkit.keys();
    Key enter = keys.enter();

    ok.onPress(listener);
    window.add(ok, toolkit.point(0, 0));
    desktop.open(window);
    desktop.type(enter);

    verify(listener).pressed(ok);
  }
}
