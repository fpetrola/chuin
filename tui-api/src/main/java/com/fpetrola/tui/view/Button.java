package com.fpetrola.tui.view;

public interface Button extends View {
  void onPress(ButtonListener listener);

  /** The text shown on it. */
  String caption();

  /** Tells every listener it was pressed, in the order they were added. */
  void press();

  /** Holds it down, as the mouse does before it is released; it is drawn down until let go. */
  void hold();

  /** Lets it come back up, pressed or not. */
  void letGo();
}
