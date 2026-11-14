package com.fpetrola.tui;

public interface Window {
  void add(Button button);

  void accept(ViewVisitor visitor);
}
