package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Draws the menu bar across the top row and, when a menu is open, its title highlighted and its box of items under
 * it, the selected item highlighted. Items of the menus that are not open are met and left alone.
 */
class MenuPainter implements MenuVisitor {
  private static final char BAR_TINT = 'w';
  private static final char CHOSEN_TINT = 'f';
  private final Screen screen;
  private final Optional<Menu> open;
  private final int selected;
  /** Where the next title goes on the top row. */
  private int column;
  /** Where the open menu's box starts. */
  private int boxColumn;
  /** The open menu's items met so far; its box is drawn again, one row taller, with each. */
  private final List<String> shown = new ArrayList<>();

  MenuPainter(Screen screen, Optional<Menu> open, int selected) {
    this.screen = screen;
    this.open = open;
    this.selected = selected;
  }

  @Override
  public void visitMenuBar(MenuBar bar) {
    int columns = screen.lines().getFirst().length();
    screen.write(0, 0, " ".repeat(columns));
    screen.paint(0, 0, columns, BAR_TINT);
  }

  @Override
  public void visitMenu(Menu menu) {
    String title = " " + menu.title() + " ";
    screen.write(column, 0, title);
    if (open.filter(menu::equals).isPresent()) {
      screen.paint(column, 0, title.length(), CHOSEN_TINT);
      boxColumn = column;
    }
    column += title.length();
  }

  @Override
  public void visitMenuItem(MenuItem item) {
    Menu menu = item.menu();
    if (open.filter(menu::equals).isEmpty()) {
      return;
    }
    shown.add(item.text());
    Area box = new TextArea(boxColumn, 1, menu.width(), shown.size() + 2);
    new RaisedFrame(box, "", BAR_TINT, Lines.SINGLE).drawOn(screen);
    for (int at = 0; at < shown.size(); at++) {
      screen.write(boxColumn + 2, 2 + at, shown.get(at));
    }
    if (selected < shown.size()) {
      screen.paint(boxColumn + 1, 2 + selected, menu.width() - 2, CHOSEN_TINT);
    }
  }

}
