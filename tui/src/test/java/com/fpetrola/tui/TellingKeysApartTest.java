package com.fpetrola.tui;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

class TellingKeysApartTest {
  private final Toolkit toolkit = new TextToolkit();
  private final KeyVisitor visitor = mock(KeyVisitor.class);

  @Test
  void enterTellsTheVisitorItIsEnter() {
    toolkit.keys().enter().accept(visitor);

    verify(visitor).enter();
  }

  @Test
  void tabTellsTheVisitorItIsTab() {
    toolkit.keys().tab().accept(visitor);

    verify(visitor).tab();
  }

  @Test
  void theKeyboardReadsTabCarriageReturnAndAnyOtherByte() {
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\t\rx".getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(3)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).tab();
    order.verify(visitor).enter();
    order.verify(visitor).other();
  }
}
