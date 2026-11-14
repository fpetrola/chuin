package com.fpetrola.tui;

/**
 * What a left click does to the view under it, once focused: presses a button, toggles a check box, selects the list
 * item or chooses the option on the row clicked; an input line is only focused.
 */
class ClickFocused implements ViewVisitor {
  /** Which row of the view was clicked, counted from its first. */
  private final int row;

  ClickFocused(int row) {
    this.row = row;
  }

  @Override
  public void visitButton(Button button) {
    button.press();
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    box.toggle();
  }

  @Override
  public void visitListBox(ListBox box) {
    box.selectShown(row);
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    group.choose(row);
  }
}
