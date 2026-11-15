package com.fpetrola.chuin;

import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * The DOS palette Turbo Vision drew with, given by the colours black, blue, green, cyan, red, grey, dark grey, bright
 * yellow and bright white: as exact RGB, which no terminal theme can change, or as the 16 named colours, for a terminal
 * without 24-bit colour, whose theme may tint them.
 */
enum DosPalette implements Palette {
  RGB((foreground, background) -> "0;38;2;" + foreground + ";48;2;" + background,
      "0;0;0", "0;0;168", "0;168;0", "0;168;168", "168;0;0", "168;168;168", "84;84;84", "252;252;84", "252;252;252"),
  NAMED((foreground, background) -> "0;" + named(foreground, 30) + ";" + named(background, 40),
      "0", "4", "2", "6", "1", "7", "8", "11", "15");

  /** The foreground and background each tint is shown in. */
  private final Map<Character, String> tints;

  /** Given how a foreground and a background are written, and each colour in that way. */
  DosPalette(BinaryOperator<String> pair, String black, String blue, String green, String cyan, String red, String grey,
      String darkGrey, String brightYellow, String brightWhite) {
    this.tints = Map.ofEntries(
        Map.entry(TintRole.BARE.letter(), pair.apply(grey, blue)),
        Map.entry(TintRole.WINDOW.letter(), pair.apply(black, grey)),
        Map.entry(TintRole.MENU.letter(), pair.apply(black, grey)),
        Map.entry(TintRole.FOCUS.letter(), pair.apply(brightWhite, green)),
        Map.entry(TintRole.BUTTON.letter(), pair.apply(black, green)),
        Map.entry(TintRole.FIELD.letter(), pair.apply(brightWhite, blue)),
        Map.entry(TintRole.LETTER.letter(), pair.apply(red, grey)),
        Map.entry(TintRole.FOCUS_LETTER.letter(), pair.apply(brightYellow, green)),
        Map.entry(TintRole.GROUP.letter(), pair.apply(black, cyan)),
        Map.entry(TintRole.GROUP_LETTER.letter(), pair.apply(brightYellow, cyan)),
        Map.entry(TintRole.SHADOW.letter(), pair.apply(darkGrey, black)));
  }

  @Override
  public String colors(char tint) {
    return tints.get(tint);
  }

  /** A named colour 0 to 15 as its code from this base, 30 for the foreground or 40 for the background. */
  private static int named(String colour, int base) {
    int number = Integer.parseInt(colour);
    return number < 8 ? base + number : base + 60 + number - 8;
  }
}
