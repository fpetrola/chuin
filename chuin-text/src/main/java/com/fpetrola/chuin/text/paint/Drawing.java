package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.screen.Screen;


/** Something drawn the same way wherever it is needed, such as a frame or a shadow, onto a screen. */
public interface Drawing {
  void drawOn(Screen screen);
}
