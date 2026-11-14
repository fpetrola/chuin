package com.fpetrola.tui;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
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
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\t\r h\u007f\u001c".getBytes(StandardCharsets.UTF_8)));
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

  @Test
  void theKeyboardReadsTheArrowKeysFromTheirEscapeSequences() {
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\u001b[A\u001b[B\u001b[C\u001b[D".getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(4)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).up();
    order.verify(visitor).down();
    order.verify(visitor).right();
    order.verify(visitor).left();
  }

  @Test
  void theKeyboardReadsTheFunctionKeysInBothWaysTerminalsSendThem() {
    String f1 = "\u001bOP";
    String f4 = "\u001bOS";
    String alsoF1 = "\u001b[11~";
    String f6 = "\u001b[17~";
    String f12 = "\u001b[24~";
    String noKey = "\u001b[16~";
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream((f1 + f4 + alsoF1 + f6 + f12 + noKey).getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(6)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).function(1);
    order.verify(visitor).function(4);
    order.verify(visitor).function(1);
    order.verify(visitor).function(6);
    order.verify(visitor).function(12);
    order.verify(visitor).other();
  }

  @Test
  void theKeyboardReadsControlLettersButLeavesTabEnterControlQAndControlHToThemselves() {
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream("\u0001\u0017\u001a\t\b".getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(5)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).control('a');
    order.verify(visitor).control('w');
    order.verify(visitor).control('z');
    order.verify(visitor).tab();
    order.verify(visitor).backspace();
  }

  @Test
  void theKeyboardHandsALeftClickToTheDesktopAndIgnoresReleasesAndOtherButtons() {
    String leftPressed = "\u001b[<0;12;3M";
    String leftReleased = "\u001b[<0;12;3m";
    String rightPressed = "\u001b[<2;5;5M";
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream((leftPressed + leftReleased + rightPressed + "\t").getBytes(StandardCharsets.UTF_8)));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    InOrder clicked = inOrder(desktop);
    clicked.verify(desktop).click(toolkit.point(11, 2));
    clicked.verify(desktop, times(3)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor, times(2)).other();
    order.verify(visitor).tab();
  }

  @Test
  void anEscapeWithNothingRightAfterItIsTheEscapeKeyAndAnEscapeBeforeALetterIsAlt() {
    Keyboard keyboard = toolkit.keyboard(new SlowStream().now("\u001bx\t").now("\u001b"));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(3)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).alt('x');
    order.verify(visitor).tab();
    order.verify(visitor).escape();
  }

  @Test
  void aSequenceWhoseBytesComeAMomentApartIsStillOneKeyButAnEscapeLeftAloneLongerIsEscape() {
    SlowStream stream = new SlowStream().now("\u001b").after(10, "[A").now("\u001b").after(300, "x");
    Keyboard keyboard = toolkit.keyboard(stream);
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(3)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).up();
    order.verify(visitor).escape();
    order.verify(visitor).character('x');
  }

  @Test
  void anEscapeBeforeAnythingButALetterIsTheEscapeKeyAndThatByteIsReadAsUsual() {
    Keyboard keyboard = toolkit.keyboard(new SlowStream().now("\u001b\u001b\u001b\t\u001b1"));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(6)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor, times(3)).escape();
    order.verify(visitor).tab();
    order.verify(visitor).escape();
    order.verify(visitor).character('1');
  }

  @Test
  void altIsOnlyForAsciiLettersSoAnEscapeBeforeANonAsciiByteIsTheEscapeKey() {
    byte[] escapeThenFirstByteOfEnye = {0x1B, (byte) 0xC3};
    Keyboard keyboard = toolkit.keyboard(new ByteArrayInputStream(escapeThenFirstByteOfEnye));
    Desktop desktop = mock(Desktop.class);
    ArgumentCaptor<Key> typed = ArgumentCaptor.forClass(Key.class);

    keyboard.typeInto(desktop);

    verify(desktop, times(2)).type(typed.capture());
    typed.getAllValues().forEach(key -> key.accept(visitor));
    InOrder order = inOrder(visitor);
    order.verify(visitor).escape();
    order.verify(visitor).other();
  }

  /** A stream whose bytes become available in chunks, each some milliseconds after the one before, as over a slow link. */
  private static class SlowStream extends InputStream {
    private final List<Integer> delays = new ArrayList<>();
    private final List<byte[]> chunks = new ArrayList<>();
    private long chunkSince = System.nanoTime();
    private int chunk;
    private int at;

    SlowStream now(String text) {
      return after(0, text);
    }

    SlowStream after(int millis, String text) {
      delays.add(millis);
      chunks.add(text.getBytes(StandardCharsets.UTF_8));
      return this;
    }

    @Override
    public int available() {
      boolean due = chunk < chunks.size() && System.nanoTime() - chunkSince >= delays.get(chunk) * 1_000_000L;
      return due ? chunks.get(chunk).length - at : 0;
    }

    @Override
    public int read() {
      if (chunk >= chunks.size()) {
        return -1;
      }
      while (available() == 0) {
        Thread.onSpinWait();
      }
      int read = chunks.get(chunk)[at++] & 0xFF;
      if (at == chunks.get(chunk).length) {
        chunk++;
        at = 0;
        chunkSince = System.nanoTime();
      }
      return read;
    }
  }
}
