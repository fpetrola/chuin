package com.fpetrola.tui;

/** A box beside a text that is either checked or not. */
public interface CheckBox extends View {
  /** The text beside the box. */
  String text();

  /** Tells the visitor whether it is checked, without a flag. */
  void acceptMark(MarkVisitor visitor);

  /** Checks it when it is not checked, and the other way round. */
  void toggle();
}
