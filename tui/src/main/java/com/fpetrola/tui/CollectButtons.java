package com.fpetrola.tui;

import java.util.List;

/** Collects the buttons of the views it visits, in the order they are visited. */
class CollectButtons implements ViewVisitor {
  private final List<Button> into;

  CollectButtons(List<Button> into) {
    this.into = into;
  }

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    into.add(button);
  }

  @Override
  public void label(String text) {
  }
}
