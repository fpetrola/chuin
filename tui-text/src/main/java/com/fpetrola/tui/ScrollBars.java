package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;

/**
 * The inside of a window, through which its views are drawn: it notes how far what is written reaches, cut off or
 * not, and once the views are drawn puts scroll bars on the window's frame where what it holds does not fit. The
 * right side gets ▲, a track ░ with the thumb ■ where the window is scrolled to, and ▼; the bottom gets ◄ and ►.
 * A click on an arrow scrolls the window a cell, and on the track a page toward the click, never past what it holds.
 */
class ScrollBars implements Screen, Drawing {
  private final Screen inside;
  private final Window window;
  private final Area frame;
  private final Point origin;
  /** The last column and row written, on the screen; one before the inside until something is. */
  private int lastColumn;
  private int lastRow;

  /** Puts its two bars, right and bottom, among the targets a click on the window's frame may fall on. */
  ScrollBars(Screen inside, Window window, List<ClickTarget> targets) {
    this.inside = inside;
    this.window = window;
    this.frame = window.area();
    this.origin = window.origin();
    targets.add(right());
    targets.add(bottom());
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
    if (heldRows() > shownRows()) {
      String bar = bar("▲", "▼", shownRows(), origin.row(), heldRows() - shownRows());
      int side = frame.column() + frame.columns() - 1;
      for (int at = 0; at < bar.length(); at++) {
        screen.write(side, frame.row() + 1 + at, bar.substring(at, at + 1));
      }
    }
    if (heldColumns() > shownColumns()) {
      screen.write(frame.column() + 1, frame.row() + frame.rows() - 1, bar("◄", "►", shownColumns(), origin.column(), heldColumns() - shownColumns()));
    }
  }

  /** The bar on the right side, which a click between its corners scrolls by rows. */
  private ClickTarget right() {
    return (cell, otherwise) -> {
      boolean between = cell.row() > frame.row() && cell.row() < frame.row() + frame.rows() - 1;
      if (cell.column() == frame.column() + frame.columns() - 1 && between && heldRows() > shownRows()) {
        window.scroll(0, step(cell.row() - frame.row() - 1, shownRows(), origin.row(), heldRows() - shownRows()));
      } else {
        otherwise.run();
      }
    };
  }

  /** The bar on the bottom side, which a click between its corners scrolls by columns. */
  private ClickTarget bottom() {
    return (cell, otherwise) -> {
      boolean between = cell.column() > frame.column() && cell.column() < frame.column() + frame.columns() - 1;
      if (cell.row() == frame.row() + frame.rows() - 1 && between && heldColumns() > shownColumns()) {
        window.scroll(step(cell.column() - frame.column() - 1, shownColumns(), origin.column(), heldColumns() - shownColumns()), 0);
      } else {
        otherwise.run();
      }
    };
  }

  /**
   * How far a click on this cell of a bar this long scrolls: a cell back on the first arrow and on the last forward, a
   * page toward the click on the track, nothing on the thumb; never past 0 nor past the most it can be scrolled.
   */
  private static int step(int at, int length, int scrolled, int most) {
    int thumb = 1 + thumb(length, scrolled, most);
    int page = length - 1;
    int back = at == 0 ? -1 : at < thumb ? -page : 0;
    int forward = at == length - 1 ? 1 : at > thumb ? page : 0;
    int wanted = scrolled + back + forward;
    return Math.max(0, Math.min(most, wanted)) - scrolled;
  }

  private int shownRows() {
    return frame.rows() - 2;
  }

  private int shownColumns() {
    return frame.columns() - 2;
  }

  private int heldRows() {
    return lastRow - frame.row() + origin.row();
  }

  private int heldColumns() {
    return lastColumn - frame.column() + origin.column();
  }

  private static int thumb(int length, int scrolled, int most) {
    return scrolled * Math.max(0, length - 3) / Math.max(1, most);
  }

  private void reach(int column, int row) {
    lastColumn = Math.max(lastColumn, column);
    lastRow = Math.max(lastRow, row);
  }

  /** An arrow at each end and a track between, with the thumb as far along it as the window is scrolled. */
  private static String bar(String first, String last, int length, int scrolled, int most) {
    int track = Math.max(0, length - 2);
    int thumb = thumb(length, scrolled, most);
    StringBuilder cells = new StringBuilder(first);
    for (int at = 0; at < track; at++) {
      cells.append(at == thumb ? "■" : "░");
    }
    return cells.append(last).toString();
  }
}
