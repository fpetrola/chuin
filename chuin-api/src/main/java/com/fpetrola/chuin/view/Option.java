package com.fpetrola.chuin.view;

/** One option of a group, of which exactly one is chosen. */
public interface Option {
  /** The group it belongs to. */
  OptionGroup group();

  String text();

  /** Makes this option the chosen one of its group. */
  void choose();
}
