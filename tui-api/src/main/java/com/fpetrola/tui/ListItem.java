package com.fpetrola.tui;

/** An item a list box shows on one of its rows. */
public interface ListItem {
  /** The list box that shows it. */
  ListBox list();

  String text();
}
