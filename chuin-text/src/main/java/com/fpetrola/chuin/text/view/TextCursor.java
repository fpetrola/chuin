package com.fpetrola.chuin.text.view;

public class TextCursor implements Cursor {
  private final int places;
  private int at;

  /** Among this many places, on the first; with none, it stays at the first all the same. */
  public TextCursor(int places) {
    this.places = places;
  }

  @Override
  public int at() {
    return at;
  }

  @Override
  public void step(int places) {
    moveTo(at + places);
  }

  @Override
  public void moveTo(int place) {
    at = Math.max(0, Math.min(places - 1, place));
  }
}
