package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.text.pointer.BarLine;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;


/** Draws the cells of a scroll bar one after the other from a cell on, down or across; blank cells leave what is there. */
class BarCells implements ViewVisitor {
  private final Screen screen;
  private final String back;
  private final String forward;
  private final BarLine line;
  /** How many cells along the line the next one is drawn. */
  private int at;

  /** Along this line, with these arrows at the ends. */
  BarCells(Screen screen, BarLine line, String back, String forward) {
    this.screen = screen;
    this.line = line;
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
    at++;
  }

  private void cell(String drawn) {
    Point cell = line.cell(at);
    screen.write(cell.column(), cell.row(), drawn);
    at++;
  }
}
