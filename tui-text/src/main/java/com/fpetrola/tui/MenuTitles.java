package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Where the menus of a bar sit and what answers: each menu by its place from the left, where its title starts, which
 * title is under a cell or answers a letter, and the rows and items of each menu.
 */
interface MenuTitles {
  int count();

  Menu menu(int at);

  int start(int at);

  OptionalInt titleAt(Point cell);

  OptionalInt answering(char letter);

  List<Optional<MenuItem>> rows(int at);

  List<MenuItem> items(int at);
}
