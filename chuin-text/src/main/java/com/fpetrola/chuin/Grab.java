package com.fpetrola.chuin;

/** Whoever follows the pointer: holding a drag, it does it at each move until the button is released. */
interface Grab {
  void hold(Dragging dragging);
}
