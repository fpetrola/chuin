package com.fpetrola.tui;

/** One option of a group of radio buttons. */
public interface Option {
  /** The group it belongs to. */
  RadioButtons group();

  String text();
}
