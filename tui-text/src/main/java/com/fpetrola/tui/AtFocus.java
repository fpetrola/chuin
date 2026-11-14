package com.fpetrola.tui;


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
  public void visitButton(Button button) {
    if (seen++ == focused) {
      action.visitButton(button);
    }
  }

  @Override
  public void visitLabel(Label label) {
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    if (seen++ == focused) {
      action.visitCheckBox(box);
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
