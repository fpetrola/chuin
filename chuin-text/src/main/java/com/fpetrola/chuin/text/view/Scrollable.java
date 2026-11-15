package com.fpetrola.chuin.text.view;

/** What a scroll bar is beside: how far it is scrolled and can be, whether it holds more than it shows, and its moves. */
interface Scrollable {
  /** Where it is, from 0 to most(): the thumb is drawn as far along the track. */
  int position();

  int most();

  /** Whether it holds more than it shows, so a bar is needed. */
  boolean overflows();

  void previous();

  void next();

  void pageUp();

  void pageDown();

  /** Goes to this position, staying between 0 and most(). */
  void moveTo(int position);
}
