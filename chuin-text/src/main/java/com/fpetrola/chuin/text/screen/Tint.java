package com.fpetrola.chuin.text.screen;

import com.fpetrola.chuin.screen.Screen;


/** What a cell is tinted for, kept by the screen as one letter, which the palette turns into colours. */
public interface Tint {
  /** Tints this many cells of a row from a cell on. */
  void paint(Screen screen, int column, int row, int cells);

  /** The letter the screen keeps for a cell of this tint, as the tests read it and the palette looks it up. */
  char letter();
}
