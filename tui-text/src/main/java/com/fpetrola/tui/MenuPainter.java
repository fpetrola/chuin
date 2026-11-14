package com.fpetrola.tui;

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
  /** How many of the open menu's items were drawn. */
  private int drawn;

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
    int inner = menu.width() - 2;
    int row = 2 + drawn;
    if (drawn == 0) {
      screen.write(boxColumn, 1, "┌" + "─".repeat(inner) + "┐");
      screen.paint(boxColumn, 1, inner + 2, BAR_TINT);
    }
    screen.write(boxColumn, row, "│ " + item.text() + " ".repeat(inner - item.text().length() - 1) + "│");
    screen.write(boxColumn, row + 1, "└" + "─".repeat(inner) + "┘");
    screen.paint(boxColumn, row, inner + 2, BAR_TINT);
    screen.paint(boxColumn, row + 1, inner + 2, BAR_TINT);
    if (drawn == selected) {
      screen.paint(boxColumn + 1, row, inner, CHOSEN_TINT);
    }
    drawn++;
  }
}
