package com.fpetrola.chuin.view;

/**
 * A bar of ▲, a track with a thumb and ▼ beside something that holds more than it shows; it scrolls what it is beside.
 * Visiting it goes on to each of its cells, first to last.
 */
public interface ScrollBar extends View {
  /** Scrolls what it is beside a step back, as ▲ does. */
  void previous();

  /** Scrolls what it is beside a step forward, as ▼ does. */
  void next();

  /** Scrolls what it is beside a page back, as the track before the thumb does. */
  void pageUp();

  /** Scrolls what it is beside a page forward, as the track after the thumb does. */
  void pageDown();

  /** Puts the thumb on this cell of the bar, counted from ▲, and what it is beside as far along; the track's ends hold it. */
  void thumbTo(int cell);
}
