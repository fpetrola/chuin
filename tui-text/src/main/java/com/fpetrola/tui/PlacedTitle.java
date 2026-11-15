package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuItem;

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
}
