package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Exact RGB needs a terminal that says it has 24-bit colour in COLORTERM (truecolor or 24bit); any other gets the same
 * palette in the 16 named colours, as the Linux console and older terminals understand.
 */
class FallingBackToSixteenColorsTest {
  @Test
  void aTerminalThatSaysItHasTrueColorGetsExactRgb() {
    String shown = shown(Map.of("COLORTERM", "truecolor"));

    assertTrue(shown.contains("\u001b[0;38;2;0;0;0;48;2;168;168;168m"), shown);
  }

  @Test
  void aTerminalThatSaysNothingGetsTheNamedColorsAndNoRgb() {
    String shown = shown(Map.of());

    assertTrue(shown.contains("\u001b[0;30;47m"), shown);
    assertTrue(shown.contains("\u001b[0;37;44m"), shown);
    assertFalse(shown.contains("38;2"), shown);
  }

  @Test
  void twentyFourBitIsTrueColorToo() {
    assertTrue(shown(Map.of("COLORTERM", "24bit")).contains("38;2"));
  }

  /** Both palettes show a window's grey with black text; dark grey and bright colours are the high named ones. */
  @Test
  void eachPaletteGivesATintItsColoursInItsOwnWay() {
    Palette rgb = DosPalette.RGB;
    Palette named = DosPalette.NAMED;

    assertTrue(rgb.colors('w').equals("0;38;2;0;0;0;48;2;168;168;168"));
    assertTrue(named.colors('w').equals("0;30;47"));
    assertTrue(named.colors('s').equals("0;90;40"));
    assertTrue(named.colors('k').equals("0;93;42"));
  }

  /** What the terminal sends for a screen with the bare desktop and a window, under this environment. */
  private static String shown(Map<String, String> environment) {
    Toolkit toolkit = new TextToolkit(environment);
    Screen screen = toolkit.screen(10, 4);
    Window window = toolkit.window("W");
    window.place(toolkit.area(0, 0, 6, 3));
    toolkit.desktop().open(window);
    toolkit.desktop().draw(screen);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    toolkit.terminal(out).show(screen);
    return out.toString(StandardCharsets.UTF_8);
  }
}
