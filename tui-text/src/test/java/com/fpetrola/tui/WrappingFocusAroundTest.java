package com.fpetrola.tui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

class WrappingFocusAroundTest {
  @Test
  void tabOnTheLastButtonGivesTheFocusBackToTheFirst() {
    Toolkit toolkit = new TextToolkit();
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
    desktop.type(toolkit.tabKey());
    desktop.type(toolkit.tabKey());
    desktop.type(toolkit.enterKey());

    verify(yesListener).pressed(yes);
    verifyNoInteractions(noListener);
  }
}
