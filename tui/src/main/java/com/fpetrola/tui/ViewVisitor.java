package com.fpetrola.tui;

import java.util.List;

interface ViewVisitor {
  void window(List<Button> buttons);

  void button(Button button, List<ButtonListener> listeners);
}
