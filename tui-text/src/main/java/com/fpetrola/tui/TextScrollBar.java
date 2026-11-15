package com.fpetrola.tui;

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
