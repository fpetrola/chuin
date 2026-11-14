package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Draws the menu bar across the top row and, when a menu is open, its title highlighted and its box of items under
 * it, the selected item highlighted. Items of the menus that are not open are met and left alone.
 */
class MenuPainter implements MenuVisitor {
  /** The menu bar and open menus: black on grey, a tint of their own apart from the windows'. */
  private static final char BAR_TINT = 'm';
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
  /** The open menu's rows met so far, in order: an item, or nothing for a separator. */
  private final List<Optional<MenuItem>> rows = new ArrayList<>();

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
    if (open.filter(item.menu()::equals).isPresent()) {
      rows.add(Optional.of(item));
      box(item.menu());
    }
  }

  @Override
  public void visitMenuSeparator(Menu menu) {
    if (open.filter(menu::equals).isPresent()) {
      rows.add(Optional.empty());
      box(menu);
    }
  }

  /** Draws the open menu's box with the rows met so far: it is drawn again, one row taller, with each. */
  private void box(Menu menu) {
    int width = menu.width();
    new RaisedFrame(new TextArea(boxColumn, 1, width, rows.size() + 2), "", BAR_TINT, Lines.SINGLE).drawOn(screen);
    int item = 0;
    for (int row = 0; row < rows.size(); row++) {
      if (rows.get(row).isEmpty()) {
        screen.write(boxColumn, 2 + row, "├" + "─".repeat(width - 2) + "┤");
      } else {
        boolean chosen = item++ == selected;
        if (chosen) {
          screen.paint(boxColumn + 1, 2 + row, width - 2, CHOSEN_TINT);
        }
        MenuItem shown = rows.get(row).get();
        Caption caption = new TextCaption(shown.text());
        screen.write(boxColumn + 2, 2 + row, caption.shown());
        caption.highlight(screen, boxColumn + 2, 2 + row, chosen ? CHOSEN_LETTER : LETTER);
        String key = shown.key().map(bound -> new KeyCaption(bound).shown()).orElse("");
        screen.write(boxColumn + width - 2 - key.length(), 2 + row, key);
      }
    }
  }
}
