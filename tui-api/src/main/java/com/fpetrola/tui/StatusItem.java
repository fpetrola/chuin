package com.fpetrola.tui;

/** An entry of the status line. */
public interface StatusItem {
  /** Its text, its key marked between tildes: "~F3~ Laden". */
  String text();

  /** The key that runs it. */
  Key key();

  /** Runs the command it was added with. */
  void run();
}
