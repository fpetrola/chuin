package com.fpetrola.tui;

public interface Button extends View {
  void onPress(ButtonListener listener);

  /** The text shown on it. */
  String caption();

  /** Tells every listener it was pressed, in the order they were added. */
  void press();
}
