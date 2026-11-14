package com.fpetrola.tui;

import java.util.Optional;

/** A list of items shown a few rows at a time, with one of them selected and a scroll bar beside them. */
public interface ListBox extends View {
  /** The item selected now, or nothing when the list has no items. */
  Optional<String> selected();

  /** How many cells its longest item takes. */
  int width();

  /** Walks the rows it shows, top to bottom: for each, its item, then its cell of the scroll bar. */
  void acceptRows(ListRowVisitor visitor);

  /** Selects the item before, staying on the first, and scrolls to keep it in sight. */
  void previous();

  /** Selects the item after, staying on the last, and scrolls to keep it in sight. */
  void next();
}
