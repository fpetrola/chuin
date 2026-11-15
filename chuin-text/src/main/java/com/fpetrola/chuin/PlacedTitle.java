package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuItem;
import com.fpetrola.chuin.screen.Screen;

import java.util.List;
import java.util.Optional;

/**
 * A menu's title where the bar puts it on the top row: it knows its menu, whether a cell falls on it, whether it
 * answers a letter, and, with its menu open under it, which item is drawn at a cell and what items there are.
 */
interface PlacedTitle {
  Menu menu();

  boolean covers(Point cell);

  boolean answers(char letter);

  /** The item drawn at this cell while the menu is open under the title, or nothing on a separator or off the menu. */
  Optional<MenuItem> itemAt(Point cell);

  List<MenuItem> items();

  /** Draws the menu open under the title, in a raised box, the item at this place among the items highlighted; a menu with no rows shows no box. */
  void drawOpen(Screen screen, int selected);
}
