package com.fpetrola.chuin;

import com.fpetrola.chuin.text.pointer.TextBarLine;
import com.fpetrola.chuin.text.view.TextCaption;
import com.fpetrola.chuin.text.view.Caption;
import com.fpetrola.chuin.text.screen.Tint;
import com.fpetrola.chuin.text.screen.TintRole;
import com.fpetrola.chuin.text.geometry.TextArea;
import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.ListItem;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.List;

/**
 * Draws rows stacked one under the other, the way a list box walks its rows and a group of options its
 * options: a list row is its item padded to the whole width, its last cell left to the scroll bar, if it has one; an option is its mark and
 * its text. The selected row is tinted when the view has the focus.
 */
class RowPainter implements ViewVisitor {
  /** The blank and the scroll bar cell after a list item. */
  private static final int BESIDE_ITEM = 2;
  private final Screen screen;
  private final Area at;
  private final boolean focused;
  /** Where focusable views were drawn; this view's place in it grows a row with every row drawn. */
  private final List<Area> drawn;
  private final int place;
  private int shown;
  /** Draws a list's scroll bar down its last column. */
  private final BarCells cells;

  RowPainter(Screen screen, Area at, boolean focused, List<Area> drawn) {
    this.screen = screen;
    this.at = at;
    this.focused = focused;
    this.drawn = drawn;
    this.place = drawn.size();
    this.cells = new BarCells(screen, new TextBarLine(new TextPoint(at.column() + at.columns() - 1, at.row()), new TextPoint(0, 1)), "▲", "▼");
    drawn.add(new TextArea(at.column(), at.row(), at.columns(), 0));
  }

  @Override
  public void visitListItem(ListItem item) {
    itemRow(item.text());
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    tint(at.columns() - BESIDE_ITEM);
    itemRow(item.text());
  }

  @Override
  public void visitNoListItem(ListBox box) {
    itemRow("");
  }

  private void itemRow(String text) {
    nextRow(text + " ".repeat(at.columns() - text.length()));
  }

  @Override
  public void visitScrollUp(ScrollBar bar) {
    cells.visitScrollUp(bar);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    cells.visitScrollTrack(bar);
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    cells.visitScrollThumb(bar);
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    cells.visitScrollDown(bar);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    cells.visitNoScrollBar(bar);
  }

  @Override
  public void visitOption(Option option) {
    TintRole.GROUP.paint(screen, at.column(), at.row() + shown, at.columns());
    optionRow("( ) ", option, TintRole.GROUP_LETTER);
  }

  @Override
  public void visitChosenOption(Option option) {
    TintRole.GROUP.paint(screen, at.column(), at.row() + shown, at.columns());
    tint(at.columns());
    optionRow("(•) ", option, focused ? TintRole.FOCUS_LETTER : TintRole.GROUP_LETTER);
  }

  /** Writes an option after its mark, its marked letter highlighted. */
  private void optionRow(String mark, Option option, Tint letter) {
    Caption caption = new TextCaption(option.text());
    caption.highlight(screen, at.column() + mark.length(), at.row() + shown, letter);
    nextRow(mark + caption.shown());
  }

  private void nextRow(String line) {
    screen.write(at.column(), at.row() + shown, line);
    shown++;
    drawn.set(place, new TextArea(at.column(), at.row(), at.columns(), shown));
  }

  private void tint(int cells) {
    if (focused) {
      TintRole.FOCUS.paint(screen, at.column(), at.row() + shown, cells);
    }
  }
}
