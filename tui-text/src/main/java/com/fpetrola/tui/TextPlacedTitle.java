package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuItem;
import com.fpetrola.tui.menu.MenuVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** A title starting at a column of the top row, as wide as its caption takes as an entry. */
class TextPlacedTitle implements PlacedTitle {
  /** The bar's row and the open menu's top frame line, above its first item. */
  private static final int ABOVE_ITEMS = 2;
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
    int row = cell.row() - ABOVE_ITEMS;
    boolean across = cell.column() >= start && cell.column() < start + menu.width();
    return across && row >= 0 && row < rows.size() ? rows.get(row) : Optional.empty();
  }

  @Override
  public List<MenuItem> items() {
    List<MenuItem> items = new ArrayList<>();
    rows().forEach(row -> row.ifPresent(items::add));
    return items;
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
