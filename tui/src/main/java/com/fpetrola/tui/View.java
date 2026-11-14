package com.fpetrola.tui;

public interface View {
  void accept(ViewVisitor visitor);
}
