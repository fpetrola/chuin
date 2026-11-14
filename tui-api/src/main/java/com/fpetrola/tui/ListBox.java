package com.fpetrola.tui;

import java.util.Optional;

public interface ListBox extends View {
  /** The item selected now, or nothing when the list has no items. */
  Optional<String> selected();
}
