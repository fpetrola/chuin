package com.fpetrola.tui;

import java.util.List;

public interface Screen {
  List<String> lines();

  void write(int column, int row, String text);

  /**
   * One string per row, one tint per cell: a letter of the palette that says how the cell is colored.
   * The palette: {@code '.'} bare desktop, {@code 'w'} inside a window, {@code 'f'} the view with the focus,
   * {@code 's'} a window's shadow.
   */
  List<String> tints();

  /** Tints this many cells of a row, starting at a column. */
  void paint(int column, int row, int columns, char tint);
}
