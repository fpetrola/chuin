package com.fpetrola.tui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;

class VisitingViewsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final ViewVisitor visitor = mock(ViewVisitor.class);

  @Test
  void aWindowHandsItsButtonsToTheVisitor() {
    Window window = toolkit.window("Saludo");
    Button ok = toolkit.button("OK");
    window.add(ok);

    window.accept(visitor);

    verify(visitor).window(List.of(ok));
  }

  @Test
  void aButtonHandsItselfAndItsListenersToTheVisitor() {
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);
    ok.onPress(listener);

    ok.accept(visitor);

    verify(visitor).button(ok, List.of(listener));
  }
}
