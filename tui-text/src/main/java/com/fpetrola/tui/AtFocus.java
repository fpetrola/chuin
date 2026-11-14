package com.fpetrola.tui;


/** Counts a window's focusable views and hands only the focused one to an action. */
class AtFocus implements ViewVisitor {
  /** A visitor that does nothing with what it meets. */
  private static final ViewVisitor NOWHERE = new ViewVisitor() {
  };
  private final int focused;
  private final ViewVisitor action;
  private int seen;
  /** Where the items of the drop down met last go: on to the action when it is the focused one, nowhere otherwise. */
  private ViewVisitor items = NOWHERE;

  AtFocus(int focused, ViewVisitor action) {
    this.focused = focused;
    this.action = action;
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

  @Override
  public void visitDropDown(DropDown dropDown) {
    boolean onFocused = seen++ == focused;
    items = onFocused ? action : NOWHERE;
    if (onFocused) {
      action.visitDropDown(dropDown);
    }
  }

  /** The items of the focused drop down's open list go on to the action too. */
  @Override
  public void visitDropDownItem(DropDownItem item) {
    items.visitDropDownItem(item);
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    items.visitHighlightedDropDownItem(item);
  }
}
