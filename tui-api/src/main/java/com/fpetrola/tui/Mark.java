package com.fpetrola.tui;

/** Whether a check box is checked, told without a flag: the mark says which one it is. */
interface Mark {
  void accept(MarkVisitor visitor);
}
