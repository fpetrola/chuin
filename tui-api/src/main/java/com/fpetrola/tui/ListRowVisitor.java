package com.fpetrola.tui;

/** Tells apart what each row a list box shows holds: an item, the selected item, or nothing; then its cell of the scroll bar. */
interface ListRowVisitor {
  default void item(String text) {
  }

  default void selectedItem(String text) {
  }

  /** A row below the last item. */
  default void noItem() {
  }

  default void barUp() {
  }

  default void barTrack() {
  }

  default void barThumb() {
  }

  default void barDown() {
  }

  /** Every item fits, so there is no scroll bar. */
  default void noBar() {
  }
}
