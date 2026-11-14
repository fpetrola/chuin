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
  void spaceTellsTheVisitorItIsSpace() {
    toolkit.keys().space().accept(visitor);

    verify(visitor).space();
  }

  @Test
  void theKeyboardReadsTabCarriageReturnSpaceALetterBackspaceAndAnyOtherByte() {
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\t\r h\u007f\u0001".getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(6)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).tab();
    order.verify(visitor).enter();
    order.verify(visitor).space();
    order.verify(visitor).character('h');
    order.verify(visitor).backspace();
    order.verify(visitor).other();
  }
}
