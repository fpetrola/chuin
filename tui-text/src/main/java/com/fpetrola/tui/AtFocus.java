package com.fpetrola.tui;

import java.util.List;

/** Counts a window's focusable views and hands only the focused one to an action. */
class AtFocus implements ViewVisitor {
  private final int focused;
  private final ViewVisitor action;
  private int seen;

  AtFocus(int focused, ViewVisitor action) {
    this.focused = focused;
    this.action = action;
  }

  @Override
  public void window(String title, Area area) {
  }

  @Override
  public void child(Point position) {
  }

  @Override
  public void button(Button button, String caption, List<ButtonListener> listeners) {
    if (seen++ == focused) {
      action.button(button, caption, listeners);
    }
  }

  @Override
  public void label(String text) {
  }

  @Override
  public void checkBox(CheckBox box, String text, Mark mark, Runnable toggle) {
    if (seen++ == focused) {
      action.checkBox(box, text, mark, toggle);
    }
  }

  @Override
  public void visitInputLine(InputLine line) {
    if (seen++ == focused) {
      action.visitInputLine(line);
    }
  }

  @Override
  public void visitListBox(ListBox box) {
    if (seen++ == focused) {
      action.visitListBox(box);
    }
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    if (seen++ == focused) {
      action.visitRadioButtons(group);
    }
  }
}
