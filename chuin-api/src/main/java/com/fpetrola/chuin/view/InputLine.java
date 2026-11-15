package com.fpetrola.chuin.view;

/** A field one line high that keeps what is typed into it. */
public interface InputLine extends View {
  /** What has been typed into it so far. */
  String text();

  /** How many cells wide it is drawn. */
  int width();

  /** Adds a character at the end of its text. */
  void type(char typed);

  /** Takes the last character of its text away; does nothing when the text is empty. */
  void erase();
}
