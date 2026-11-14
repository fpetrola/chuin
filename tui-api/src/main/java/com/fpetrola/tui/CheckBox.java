package com.fpetrola.tui;

/** A box beside a text that is either checked or not; visiting it goes on to its mark. */
public interface CheckBox extends View {
  /** The text beside the box. */
  String text();

  /** Checks it when it is not checked, and the other way round. */
  void toggle();
}
