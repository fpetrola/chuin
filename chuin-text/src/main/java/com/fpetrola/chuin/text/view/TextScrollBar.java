package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;


/** A bar this many cells long beside what it scrolls: ▲, a track with the thumb as far along as that is, and ▼. */
class TextScrollBar implements ScrollBar {
  /** ▲, at least one cell of track, and ▼. */
  private static final int ENDS = 3;
  private final Scrollable owner;
  private final int length;

  TextScrollBar(Scrollable owner, int length) {
    this.owner = owner;
    this.length = length;
  }

  /** One cell across and its length down; laid across a window's bottom it is drawn the other way. */
  @Override
  public Size size() {
    return new TextSize(1, length);
  }

  @Override
  public void previous() {
    owner.previous();
  }

  @Override
  public void next() {
    owner.next();
  }

  @Override
  public void pageUp() {
    owner.pageUp();
  }

  @Override
  public void pageDown() {
    owner.pageDown();
  }

  /** The track runs from the cell after ▲ to the one before ▼; a cell past either end holds the thumb there. */
  @Override
  public void thumbTo(int cell) {
    int track = Math.max(0, Math.min(length - ENDS, cell - 1));
    owner.moveTo((track * owner.most() + (length - ENDS) / 2) / Math.max(1, length - ENDS));
  }

  /** Each cell, first to last: ▲, the track with its thumb, ▼; blank cells when what it is beside shows all it holds. */
  @Override
  public void accept(ViewVisitor visitor) {
    if (!owner.overflows()) {
      for (int cell = 0; cell < length; cell++) {
        visitor.visitNoScrollBar(this);
      }
      return;
    }
    int thumb = 1 + owner.position() * (length - ENDS) / Math.max(1, owner.most());
    visitor.visitScrollUp(this);
    for (int cell = 1; cell < length - 1; cell++) {
      visitTrack(visitor, cell == thumb);
    }
    visitor.visitScrollDown(this);
  }

  private void visitTrack(ViewVisitor visitor, boolean thumb) {
    if (thumb) {
      visitor.visitScrollThumb(this);
    } else {
      visitor.visitScrollTrack(this);
    }
  }
}
