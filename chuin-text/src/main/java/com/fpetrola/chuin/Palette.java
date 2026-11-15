package com.fpetrola.chuin;

/** The colours a terminal shows each tint in. */
interface Palette {
  /** The ANSI parameters that set the foreground and the background of this tint. */
  String colors(char tint);
}
