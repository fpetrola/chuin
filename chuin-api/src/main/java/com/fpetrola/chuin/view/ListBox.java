package com.fpetrola.chuin.view;

import java.util.Optional;

/** A list of items shown a few rows at a time, with one of them selected and a scroll bar beside them. */
/** Visiting it goes on to each row it shows and its cell of the scroll bar. */
public interface ListBox extends View {
  /** The item selected now, or nothing when the list has no items. */
  Optional<String> selected();

  /** How many cells it takes across: its longest item, a blank, and the scroll bar. */
  int width();


  /** Selects the item before, staying on the first, and scrolls to keep it in sight. */
  void previous();

  /** Selects the item after, staying on the last, and scrolls to keep it in sight. */
  void next();

  /** Selects the item a page before, a page being one fewer than the rows shown, staying on the first. */
  void pageUp();

  /** Selects the item a page after, a page being one fewer than the rows shown, staying on the last. */
  void pageDown();

}
