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
  /** What a click off the bars goes to: a bar walking no cell, so the click goes otherwise. */
  private static final ScrollBar NO_BAR = new TextScrollBar(new WindowAxis(() -> 0, cells -> { }, 0, 0), 0);
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

  /** Draws the bars on the frame where what was written did not fit inside it; a bar that is not needed draws nothing. */
  @Override
  public void drawOn(Screen screen) {
    Point right = new TextPoint(frame.column() + frame.columns() - 1, frame.row() + 1);
    Point bottom = new TextPoint(frame.column() + 1, frame.row() + frame.rows() - 1);
    rightBar().accept(new BarCells(screen, right, new TextPoint(0, 1), "▲", "▼"));
    bottomBar().accept(new BarCells(screen, bottom, new TextPoint(1, 0), "◄", "►"));
  }

  /** The bar on the right side, which a click between its corners scrolls by rows. */
  private ClickTarget right() {
    return (cell, otherwise) -> {
      boolean between = cell.row() > frame.row() && cell.row() < frame.row() + frame.rows() - 1;
      boolean onSide = cell.column() == frame.column() + frame.columns() - 1 && between;
      click(onSide ? rightBar() : NO_BAR, cell.row() - frame.row() - 1, otherwise);
    };
  }

  /** The bar on the bottom side, which a click between its corners scrolls by columns. */
  private ClickTarget bottom() {
    return (cell, otherwise) -> {
      boolean between = cell.column() > frame.column() && cell.column() < frame.column() + frame.columns() - 1;
      boolean onSide = cell.row() == frame.row() + frame.rows() - 1 && between;
      click(onSide ? bottomBar() : NO_BAR, cell.column() - frame.column() - 1, otherwise);
    };
  }

  /** Clicks this cell of a bar; when the bar does nothing with it, as one that is not needed, runs otherwise. */
  private static void click(ScrollBar bar, int at, Runnable otherwise) {
    Settle pending = new PendingAction(otherwise);
    bar.accept(new BarClick(at, pending));
    pending.settle();
  }

  private ScrollBar rightBar() {
    return new TextScrollBar(new WindowAxis(() -> window.origin().row(), rows -> window.scroll(0, rows), shownRows(), heldRows()), shownRows());
  }

  private ScrollBar bottomBar() {
    return new TextScrollBar(new WindowAxis(() -> window.origin().column(), columns -> window.scroll(columns, 0), shownColumns(), heldColumns()), shownColumns());
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

  private void reach(int column, int row) {
    lastColumn = Math.max(lastColumn, column);
    lastRow = Math.max(lastRow, row);
  }
}
