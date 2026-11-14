package com.fpetrola.tui;

import java.util.List;

public interface Screen {
  List<String> lines();

  void write(int column, int row, String text);
}
