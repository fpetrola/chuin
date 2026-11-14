package com.fpetrola.tui;

public interface Label extends View {
  void text(String text);

  /** The text it shows now. */
  String text();
}
