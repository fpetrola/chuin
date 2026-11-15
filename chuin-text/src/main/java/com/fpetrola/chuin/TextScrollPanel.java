package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ScrollPanel;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.function.Supplier;

/** Keeps its origin within what it holds; its size is given, or follows something else's, as a window's inside does. */
class TextScrollPanel implements ScrollPanel {
  /** The last column and the last row, which its bars take. */
  private static final int BAR = 1;
  private final View content;
  private final Supplier<Size> size;
  private Point origin = new TextPoint(0, 0);

  TextScrollPanel(View content, Supplier<Size> size) {
    this.content = content;
    this.size = size;
  }

  @Override
  public Point origin() {
    return origin;
  }

  @Override
  public void scroll(int columns, int rows) {
    origin = new TextPoint(within(origin.column() + columns, mostColumns()), within(origin.row() + rows, mostRows()));
  }

  @Override
  public ScrollBar vertical() {
    return new TextScrollBar(new PanelAxis(() -> origin.row(), rows -> scroll(0, rows), shownRows(), content.size().rows()), shownRows());
  }

  @Override
  public ScrollBar horizontal() {
    return new TextScrollBar(new PanelAxis(() -> origin.column(), columns -> scroll(columns, 0), shownColumns(), content.size().columns()), shownColumns());
  }

  @Override
  public Size size() {
    return size.get();
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitScrollPanel(this);
    content.accept(visitor);
    visitor.leaveScrollPanel(this);
  }

  private int shownColumns() {
    return size().columns() - BAR;
  }

  private int shownRows() {
    return size().rows() - BAR;
  }

  private int mostColumns() {
    return Math.max(0, content.size().columns() - shownColumns());
  }

  private int mostRows() {
    return Math.max(0, content.size().rows() - shownRows());
  }

  private static int within(int wanted, int most) {
    return Math.max(0, Math.min(most, wanted));
  }
}
