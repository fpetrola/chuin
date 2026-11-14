package com.fpetrola.tui;

import java.util.ArrayList;
import java.util.List;

class TextStatusLine implements StatusLine {
  private final List<StatusItem> items = new ArrayList<>();

  @Override
  public void add(String text, Key key, Runnable command) {
    items.add(new TextStatusItem(text, key, command));
  }

  @Override
  public void accept(StatusVisitor visitor) {
    visitor.visitStatusLine(this);
    items.forEach(visitor::visitStatusItem);
  }
}
