package com.fpetrola.chuin.text.screen;

/** The colours a terminal shows each tint in. */
public interface Palette {
  /** The ANSI parameters that set the foreground and the background of this tint. */
  String colors(char tint);
}
