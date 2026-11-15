package com.fpetrola.tui.demo;

import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.Toolkit;

/** One of the examples the demo runs: what it is called on the command line, and the windows, menus and status line it puts on a desktop. */
interface Example {
  /** The name demo.sh takes to run it, as in {@code ./demo.sh lists}. */
  String name();

  /** Puts its windows, menu bar and status line on a desktop made by this toolkit. */
  void build(Toolkit toolkit, Desktop desktop);
}
