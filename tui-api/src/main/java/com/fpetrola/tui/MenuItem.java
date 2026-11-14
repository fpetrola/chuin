package com.fpetrola.tui;

/** An item of a menu. */
public interface MenuItem {
  /** The menu it belongs to. */
  Menu menu();

  String text();

  /** Runs the command it was added with. */
  void run();
}
