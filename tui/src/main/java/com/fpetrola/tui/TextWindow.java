package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextWindow implements Window {
  private final List<Button> buttons = new ArrayList<>();

  @Override
  public void add(Button button) {
    buttons.add(button);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.window(List.copyOf(buttons));
  }
}
