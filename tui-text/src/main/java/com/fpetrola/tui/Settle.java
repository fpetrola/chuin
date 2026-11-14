package com.fpetrola.tui;

/** Something a visit leaves pending until it is over: once every view was met, it runs what is still due. */
interface Settle {
  void settle();
}
