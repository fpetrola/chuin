package com.fpetrola.tui;

import java.util.Optional;

public interface Label extends View {
  void text(String text);

  /** The text it shows now. */
  String text();

  /** The view it is the label of, if any. */
  Optional<View> linked();
}
