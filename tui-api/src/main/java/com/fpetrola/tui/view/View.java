package com.fpetrola.tui.view;

import com.fpetrola.tui.geometry.Size;


public interface View {
  void accept(ViewVisitor visitor);

  /** How many columns and rows it takes where it is placed, as it is drawn now. */
  Size size();
}
