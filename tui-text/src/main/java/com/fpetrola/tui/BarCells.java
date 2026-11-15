package com.fpetrola.tui;

/** Draws the cells of a scroll bar one after the other from a cell on, down or across; blank cells leave what is there. */
class BarCells implements ViewVisitor {
  private final Screen screen;
  private final String back;
  private final String forward;
  /** How far each next cell is from the one before. */
  private final Point step;
  /** The cell the next one is drawn on. */
  private Point at;

  /** From this cell, each next cell this many columns and rows further, with these arrows at the ends. */
  BarCells(Screen screen, Point from, Point step, String back, String forward) {
    this.screen = screen;
    this.at = from;
    this.step = step;
    this.back = back;
    this.forward = forward;
  }

  @Override
  public void visitScrollUp(ScrollBar bar) {
    cell(back);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    cell("░");
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    cell("■");
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    cell(forward);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    advance();
  }

  private void cell(String drawn) {
    screen.write(at.column(), at.row(), drawn);
    advance();
  }

  private void advance() {
    at = new TextPoint(at.column() + step.column(), at.row() + step.row());
  }
}
