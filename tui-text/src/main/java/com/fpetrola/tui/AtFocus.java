package com.fpetrola.tui;


/** Counts a window's focusable views and hands only the focused one to an action. */
class AtFocus implements ViewVisitor {
  /** A visitor that does nothing with what it meets. */
  private static final ViewVisitor NOWHERE = new ViewVisitor() {
  };
  private final int focused;
  private final ViewVisitor action;
  private int seen;
  /** Where the parts of the view met last go, a drop down's items, a group's options or a list's rows and scroll bar: on to the action when it is the focused one, nowhere otherwise. */
  private ViewVisitor parts = NOWHERE;

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
    boolean onFocused = seen++ == focused;
    parts = onFocused ? action : NOWHERE;
    if (onFocused) {
      action.visitListBox(box);
    }
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    boolean onFocused = seen++ == focused;
    parts = onFocused ? action : NOWHERE;
    if (onFocused) {
      action.visitRadioButtons(group);
    }
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    boolean onFocused = seen++ == focused;
    parts = onFocused ? action : NOWHERE;
    if (onFocused) {
      action.visitDropDown(dropDown);
    }
  }

  /** The items of the focused drop down's open list go on to the action too. */
  @Override
  public void visitDropDownItem(DropDownItem item) {
    parts.visitDropDownItem(item);
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    parts.visitHighlightedDropDownItem(item);
  }

  /** The cells of the focused list's scroll bar go on to the action too. */
  @Override
  public void visitScrollUp(ListBox box) {
    parts.visitScrollUp(box);
  }

  @Override
  public void visitScrollTrack(ListBox box) {
    parts.visitScrollTrack(box);
  }

  @Override
  public void visitScrollThumb(ListBox box) {
    parts.visitScrollThumb(box);
  }

  @Override
  public void visitScrollDown(ListBox box) {
    parts.visitScrollDown(box);
  }

  @Override
  public void visitNoScrollBar(ListBox box) {
    parts.visitNoScrollBar(box);
  }

  /** The options of the focused group and the rows of the focused list go on to the action too. */
  @Override
  public void visitOption(Option option) {
    parts.visitOption(option);
  }

  @Override
  public void visitChosenOption(Option option) {
    parts.visitChosenOption(option);
  }

  @Override
  public void visitListItem(ListItem item) {
    parts.visitListItem(item);
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    parts.visitSelectedListItem(item);
  }
}
