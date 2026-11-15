package com.fpetrola.tui;

import java.util.List;

/** Collects the views that can take the focus, in the order they are visited. */
class CollectFocusable implements ViewVisitor {
  private final List<View> into;

  CollectFocusable(List<View> into) {
    this.into = into;
  }

  @Override
  public void visitButton(Button button) {
    into.add(button);
  }

  @Override
  public void visitLabel(Label label) {
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    into.add(box);
  }

  @Override
  public void visitInputLine(InputLine line) {
    into.add(line);
  }

  @Override
  public void visitListBox(ListBox box) {
    into.add(box);
  }

  @Override
  public void visitOptionGroup(OptionGroup group) {
    into.add(group);
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    into.add(dropDown);
  }
}
