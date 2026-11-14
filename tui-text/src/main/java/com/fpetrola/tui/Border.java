package com.fpetrola.tui;

/** The lines a frame is drawn with, each row of it drawn by the border itself. */
interface Border {
  /** The top row: a corner, the line across with a label in the middle, a corner. */
  String top(String label, int inner);

  /** A row between the top and the bottom: the line down, a blank inside, the line down. */
  String middle(int inner);

  /** The bottom row: a corner, the line across, a corner. */
  String bottom(int inner);
}
