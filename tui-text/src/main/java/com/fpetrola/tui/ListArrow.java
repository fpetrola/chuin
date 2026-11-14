package com.fpetrola.tui;

import java.util.function.BinaryOperator;

/** What ↑ or ↓ does to the focused view: a list selects the item before or after, any other view lets the window move. Every focusable view must be overridden here, or ↑ and ↓ do nothing while it has the focus. */
class ListArrow implements ViewVisitor {
  private final BinaryOperator<Runnable> choose;
  private final Runnable otherwise;

  ListArrow(BinaryOperator<Runnable> choose, Runnable otherwise) {
    this.choose = choose;
    this.otherwise = otherwise;
  }

  @Override
  public void visitButton(Button button) {
    otherwise.run();
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    otherwise.run();
  }

  @Override
  public void visitInputLine(InputLine line) {
    otherwise.run();
  }

  @Override
  public void visitListBox(ListBox box) {
    choose.apply(box::previous, box::next).run();
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    choose.apply(group::previous, group::next).run();
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    choose.apply(dropDown::up, dropDown::down).run();
  }
}
