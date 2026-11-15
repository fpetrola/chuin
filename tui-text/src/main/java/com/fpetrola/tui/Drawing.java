package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;


/** Something drawn the same way wherever it is needed, such as a frame or a shadow, onto a screen. */
interface Drawing {
  void drawOn(Screen screen);
}
