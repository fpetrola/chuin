package com.fpetrola.tui.menu;

import com.fpetrola.tui.input.Key;

import java.util.Optional;

/** An item of a menu. */
public interface MenuItem {
  /** The menu it belongs to. */
  Menu menu();

  String text();

  /** The key that runs it from anywhere, shown on its right, if it has one. */
  Optional<Key> key();

  /** Runs the command it was added with. */
  void run();
}
