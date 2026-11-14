package com.fpetrola.tui;

/**
 * A text with a part marked between tildes, shown without the marks and the marked part highlighted: one letter,
 * "~D~atei", which Alt with that letter reaches, or several, "~F3~ Laden", which are only highlighted.
 */
interface Caption {
  /** The text as it is shown: without the marking tildes, "~~" as one tilde. */
  String shown();

  /** Paints the marked letter of the text shown from this cell on in the tint of a highlighted letter; nothing when none is marked. */
  void highlight(Screen screen, int column, int row, char tint);

  /** Whether this letter, in upper or lower case, is the one marked; a mark of several letters answers to none. */
  boolean answers(char letter);
}
