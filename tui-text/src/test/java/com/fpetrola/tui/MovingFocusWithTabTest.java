package com.fpetrola.tui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

class MovingFocusWithTabTest {
  @Test
  void tabMovesTheFocusToTheNextButtonSoEnterPressesThatOne() {
    Toolkit toolkit = new TextToolkit();
    Keys keys = toolkit.keys();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("A");
    Button yes = toolkit.button("Sí");
    Button no = toolkit.button("No");
    ButtonListener yesListener = mock(ButtonListener.class);
    ButtonListener noListener = mock(ButtonListener.class);

    yes.onPress(yesListener);
    no.onPress(noListener);
    window.add(yes, toolkit.point(1, 1));
    window.add(no, toolkit.point(8, 1));
    desktop.open(window);
    desktop.type(keys.tab());
    desktop.type(keys.enter());

    verify(noListener).pressed(no);
    verifyNoInteractions(yesListener);
  }
}
