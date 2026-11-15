package com.fpetrola.chuin.text.menu;

import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.StatusItem;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.menu.StatusVisitor;

import java.util.ArrayList;
import java.util.List;

public class TextStatusLine implements StatusLine {
  private final List<StatusItem> items = new ArrayList<>();

  /** With no entry yet: the line is blank until one is added. */
  public TextStatusLine() {
  }

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
