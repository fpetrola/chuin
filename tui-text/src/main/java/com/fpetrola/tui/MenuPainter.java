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
  /** A highlighted letter on grey, and on the green of the open title and the chosen item. */
  private static final char LETTER = 'h';
  private static final char CHOSEN_LETTER = 'k';
  private final Screen screen;
  private final Optional<Menu> open;
  private final int selected;
  /** The top row, which takes the titles left to right. */
  private final EntryRow titles;
  /** Where the open menu's box starts. */
  private int boxColumn;
  /** The open menu's items met so far; its box is drawn again, one row taller, with each. */
  private final List<Caption> shown = new ArrayList<>();

  MenuPainter(Screen screen, Optional<Menu> open, int selected) {
    this.screen = screen;
    this.open = open;
    this.selected = selected;
    this.titles = new TextEntryRow(screen, 0);
  }

  @Override
  public void visitMenuBar(MenuBar bar) {
    int columns = screen.lines().getFirst().length();
    screen.write(0, 0, " ".repeat(columns));
    screen.paint(0, 0, columns, BAR_TINT);
  }

  @Override
  public void visitMenu(Menu menu) {
    boolean chosen = open.filter(menu::equals).isPresent();
    boxColumn = chosen ? titles.next() : boxColumn;
    titles.put(new TextCaption(menu.title()), chosen ? CHOSEN_TINT : BAR_TINT, chosen ? CHOSEN_LETTER : LETTER);
  }

  @Override
  public void visitMenuItem(MenuItem item) {
    Menu menu = item.menu();
    if (open.filter(menu::equals).isEmpty()) {
      return;
    }
    shown.add(new TextCaption(item.text()));
    Area box = new TextArea(boxColumn, 1, menu.width(), shown.size() + 2);
    new RaisedFrame(box, "", BAR_TINT, Lines.SINGLE).drawOn(screen);
    if (selected < shown.size()) {
      screen.paint(boxColumn + 1, 2 + selected, menu.width() - 2, CHOSEN_TINT);
    }
    for (int at = 0; at < shown.size(); at++) {
      screen.write(boxColumn + 2, 2 + at, shown.get(at).shown());
      shown.get(at).highlight(screen, boxColumn + 2, 2 + at, at == selected ? CHOSEN_LETTER : LETTER);
    }
  }

}
