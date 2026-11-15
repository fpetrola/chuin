package com.fpetrola.chuin.text.screen;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.text.geometry.TextArea;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/** The screens and the terminal the rest of chuin-text makes for itself, from outside their package. */
class MakingScreensTest {
  @Test
  void aNewScreenIsBlankAndCutsOffWhatFallsOutsideIt() {
    Screen screen = new TextScreen(5, 2);

    screen.write(3, 1, "abcdef");

    assertEquals(List.of("     ", "   ab"), screen.lines());
  }

  @Test
  void aScreenSeenThroughAnAreaKeepsOnlyWhatIsWrittenInsideTheArea() {
    Screen screen = new TextScreen(6, 3);
    Screen clipped = new ClippedScreen(screen, new TextArea(1, 1, 3, 1));

    clipped.write(0, 0, "xxxxxx");
    clipped.write(0, 1, "abcdef");

    assertEquals(List.of("      ", " bcd  ", "      "), screen.lines());
  }

  @Test
  void aTerminalShowsWhatTheScreenHolds() {
    Screen screen = new TextScreen(5, 1);
    screen.write(0, 0, "hello");
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    new AnsiTerminal(out, DosPalette.NAMED).show(screen);

    assertTrue(out.toString(StandardCharsets.UTF_8).contains("hello"));
  }
}
