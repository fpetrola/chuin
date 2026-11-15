package com.fpetrola.tui;

import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuBar;
import com.fpetrola.tui.menu.MenuVisitor;
import com.fpetrola.tui.screen.Screen;

import java.util.Optional;

/** Draws the menu bar across the top row, the open menu's title highlighted; the open menu's box is its title's to draw. */
class MenuPainter implements MenuVisitor {
  private final Screen screen;
  private final Optional<Menu> open;
  /** The top row, which takes the titles left to right. */
  private final EntryRow titles;

  MenuPainter(Screen screen, Optional<Menu> open) {
    this.screen = screen;
    this.open = open;
    this.titles = new TextEntryRow(screen, 0);
  }

  @Override
  public void visitMenuBar(MenuBar bar) {
    int columns = screen.lines().getFirst().length();
    screen.write(0, 0, " ".repeat(columns));
    TintRole.MENU.paint(screen, 0, 0, columns);
  }

  @Override
  public void visitMenu(Menu menu) {
    boolean chosen = open.filter(menu::equals).isPresent();
    titles.put(new TextCaption(menu.title()), chosen ? TintRole.FOCUS : TintRole.MENU, chosen ? TintRole.FOCUS_LETTER : TintRole.LETTER);
  }
}
