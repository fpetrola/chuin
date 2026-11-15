package com.fpetrola.tui;

import com.fpetrola.tui.view.Button;
import com.fpetrola.tui.view.ViewVisitor;


/** A release of the mouse: the button it held is let go, and pressed when the release is over it. */
class LetGo implements ViewVisitor {
  /** What the release does once the walk is over. */
  private final Settle pending;
  /** Whether the release fell on the view the press was on. */
  private final boolean over;

  LetGo(boolean over, Settle pending) {
    this.over = over;
    this.pending = pending;
  }

  @Override
  public void visitHeldButton(Button button) {
    pending.note(() -> {
      button.letGo();
      if (over) {
        button.press();
      }
    });
  }
}
