package com.fpetrola.chuin.text.pointer;

/** Whoever follows the pointer: holding a drag, it does it at each move until the button is released. */
public interface Grab {
  void hold(Dragging dragging);
}
