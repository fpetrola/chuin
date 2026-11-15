package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;

/**
 * The inside of a window, through which its views are drawn: it notes how far what is written reaches, cut off or
 * not, and once the views are drawn puts scroll bars on the window's frame where what it holds does not fit. The
 * right side gets ▲, a track ░ with the thumb ■ where the window is scrolled to, and ▼; the bottom gets ◄ and ►.
 */
class ScrollBars implements Screen, Drawing {
  private final Screen inside;
  private final Area frame;
  private final Point origin;
  /** The last column and row written, on the screen; one before the inside until something is. */
  private int lastColumn;
  private int lastRow;

  ScrollBars(Screen inside, Area frame, Point origin) {
    this.inside = inside;
    this.frame = frame;
    this.origin = origin;
    this.lastColumn = frame.column();
    this.lastRow = frame.row();
  }

  @Override
  public void write(int column, int row, String text) {
    reach(column + text.length() - 1, row);
    inside.write(column, row, text);
  }

  @Override
  public void paint(int column, int row, int columns, char tint) {
    reach(column + columns - 1, row);
    inside.paint(column, row, columns, tint);
  }

  @Override
  public List<String> lines() {
    return inside.lines();
  }

  @Override
  public List<String> tints() {
    return inside.tints();
  }

  @Override
  public void showCursorAt(Point cell) {
    inside.showCursorAt(cell);
  }

  @Override
  public void hideCursor() {
    inside.hideCursor();
  }

  @Override
  public Optional<Point> cursor() {
    return inside.cursor();
  }

  /** Draws the bars on the frame where what was written did not fit inside it. */
  @Override
  public void drawOn(Screen screen) {
    int shownRows = frame.rows() - 2;
    int shownColumns = frame.columns() - 2;
    int heldRows = lastRow - frame.row() + origin.row();
    int heldColumns = lastColumn - frame.column() + origin.column();
    if (heldRows > shownRows) {
      String bar = bar("▲", "▼", shownRows, origin.row(), heldRows - shownRows);
      int side = frame.column() + frame.columns() - 1;
      for (int at = 0; at < bar.length(); at++) {
        screen.write(side, frame.row() + 1 + at, bar.substring(at, at + 1));
      }
    }
    if (heldColumns > shownColumns) {
      screen.write(frame.column() + 1, frame.row() + frame.rows() - 1, bar("◄", "►", shownColumns, origin.column(), heldColumns - shownColumns));
    }
  }

  private void reach(int column, int row) {
    lastColumn = Math.max(lastColumn, column);
    lastRow = Math.max(lastRow, row);
  }

  /** An arrow at each end and a track between, with the thumb as far along it as the window is scrolled. */
  private static String bar(String first, String last, int length, int scrolled, int most) {
    int track = Math.max(0, length - 2);
    int thumb = scrolled * Math.max(0, track - 1) / Math.max(1, most);
    StringBuilder cells = new StringBuilder(first);
    for (int at = 0; at < track; at++) {
      cells.append(at == thumb ? "■" : "░");
    }
    return cells.append(last).toString();
  }
}
