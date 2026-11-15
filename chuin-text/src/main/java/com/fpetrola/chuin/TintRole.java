package com.fpetrola.chuin;

import com.fpetrola.chuin.screen.Screen;

/**
 * What each cell is tinted for, one letter a role, as the screen keeps it and the tests read it; the palette says which
 * colours each is shown in.
 */
enum TintRole implements Tint {
  /** The desktop and whatever nothing else tints: grey on blue. */
  BARE('.'),
  /** A window's inside and frame: black on grey. */
  WINDOW('w'),
  /** The menu bar, an open menu, the status line and an open list: black on grey. */
  MENU('m'),
  /** The focused view or the chosen row: bright white on green. */
  FOCUS('f'),
  /** A button's face: black on green. */
  BUTTON('b'),
  /** An input line: bright white on blue. */
  FIELD('i'),
  /** A marked letter on grey: red. */
  LETTER('h'),
  /** A marked letter on green: bright yellow. */
  FOCUS_LETTER('k'),
  /** The field a group of options sits on: black on cyan. */
  GROUP('c'),
  /** A marked letter on cyan: bright yellow. */
  GROUP_LETTER('q'),
  /** A shadow: dark grey on black. */
  SHADOW('s');

  private final char letter;

  TintRole(char letter) {
    this.letter = letter;
  }

  @Override
  public void paint(Screen screen, int column, int row, int cells) {
    screen.paint(column, row, cells, letter);
  }

  @Override
  public char letter() {
    return letter;
  }
}
