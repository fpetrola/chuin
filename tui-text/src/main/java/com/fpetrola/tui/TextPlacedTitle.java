package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuItem;
import com.fpetrola.tui.menu.MenuVisitor;
import com.fpetrola.tui.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** A title starting at a column of the top row, as wide as its caption takes as an entry. */
class TextPlacedTitle implements PlacedTitle {
  /** The bar's row, above the open menu's frame. */
  private static final int BAR_ROWS = 1;
  private final Menu menu;
  private final int start;

  TextPlacedTitle(Menu menu, int start) {
    this.menu = menu;
    this.start = start;
  }

  @Override
  public Menu menu() {
    return menu;
  }

  @Override
  public boolean covers(Point cell) {
    int width = new TextCaption(menu.title()).entryWidth();
    return cell.row() == 0 && cell.column() >= start && cell.column() < start + width;
  }

  @Override
  public boolean answers(char letter) {
    return new TextCaption(menu.title()).answers(letter);
  }

  @Override
  public Optional<MenuItem> itemAt(Point cell) {
    List<Optional<MenuItem>> rows = rows();
    int row = cell.row() - items(rows.size()).row();
    Area framed = Line.SINGLE.around(items(rows.size()));
    boolean across = cell.column() >= framed.column() && cell.column() < framed.column() + framed.columns();
    return across && row >= 0 && row < rows.size() ? rows.get(row) : Optional.empty();
  }

  @Override
  public List<MenuItem> items() {
    List<MenuItem> items = new ArrayList<>();
    rows().forEach(row -> row.ifPresent(items::add));
    return items;
  }

  @Override
  public void drawOpen(Screen screen, int selected) {
    List<Optional<MenuItem>> rows = rows();
    if (rows.isEmpty()) {
      return;
    }
    Area held = items(rows.size());
    Area framed = Line.SINGLE.around(held);
    new RaisedFrame(framed, "", TintRole.MENU, Line.SINGLE).drawOn(screen);
    int item = 0;
    for (int row = 0; row < rows.size(); row++) {
      int line = held.row() + row;
      if (rows.get(row).isEmpty()) {
        screen.write(framed.column(), line, "├" + "─".repeat(held.columns()) + "┤");
      } else {
        boolean chosen = item++ == selected;
        if (chosen) {
          TintRole.FOCUS.paint(screen, held.column(), line, held.columns());
        }
        MenuItem shown = rows.get(row).get();
        Caption caption = new TextCaption(shown.text());
        screen.write(held.column() + 1, line, caption.shown());
        caption.highlight(screen, held.column() + 1, line, chosen ? TintRole.FOCUS_LETTER : TintRole.LETTER);
        String key = shown.key().map(bound -> new KeyCaption(bound).shown()).orElse("");
        screen.write(held.column() + held.columns() - 1 - key.length(), line, key);
      }
    }
  }

  /** What the open menu's frame holds, this many rows of items and separators: under the bar and the frame's top line. */
  private Area items(int rows) {
    return Line.SINGLE.holding(new TextPoint(start, BAR_ROWS), new TextSize(menu.width(), rows));
  }

  /** The rows of the menu, top to bottom: an item, or nothing for a separator. */
  private List<Optional<MenuItem>> rows() {
    List<Optional<MenuItem>> rows = new ArrayList<>();
    menu.accept(new MenuVisitor() {
      @Override
      public void visitMenuItem(MenuItem each) {
        rows.add(Optional.of(each));
      }

      @Override
      public void visitMenuSeparator(Menu separated) {
        rows.add(Optional.empty());
      }
    });
    return rows;
  }
}
