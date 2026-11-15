package com.fpetrola.chuin.view;

import com.fpetrola.chuin.geometry.Size;


public interface View {
  void accept(ViewVisitor visitor);

  /** How many columns and rows it takes where it is placed, as it is drawn now. */
  Size size();
}
