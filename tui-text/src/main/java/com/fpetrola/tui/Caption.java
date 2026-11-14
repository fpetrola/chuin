package com.fpetrola.tui;

/** A text with one letter marked between tildes, "~D~atei", shown without the marks and its letter highlighted. */
interface Caption {
  /** The text as it is shown: without the marking tildes, "~~" as one tilde. */
  String shown();

  /** Paints the marked letter of the text shown from this cell on in the tint of a highlighted letter; nothing when none is marked. */
  void highlight(Screen screen, int column, int row, char tint);
}
