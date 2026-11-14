package com.fpetrola.tui;

/** An item on one of a list box's rows, with the list box that shows it. */
record TextListItem(ListBox list, String text) implements ListItem {
}
