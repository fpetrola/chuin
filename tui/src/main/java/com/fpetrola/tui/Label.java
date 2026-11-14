package com.fpetrola.tui;

public interface Label {
  void text(String text);

  void accept(ViewVisitor visitor);
}
