package com.fpetrola.chuin;

import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.ButtonListener;
import com.fpetrola.chuin.view.Window;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class TypingOnAKeyboardTest {
  @Test
  void onlyEnterReadFromTheKeyboardPressesTheButton() {
    Toolkit toolkit = new TextToolkit();
    InputStream typed = new ByteArrayInputStream("x\r".getBytes(StandardCharsets.UTF_8));
    Keyboard keyboard = toolkit.keyboard(typed);
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Saludo");
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);

    ok.onPress(listener);
    window.add(ok, toolkit.point(0, 0));
    desktop.open(window);
    keyboard.typeInto(desktop);

    verify(listener, times(1)).pressed(ok);
  }
}
