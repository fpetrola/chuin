package com.fpetrola.tui;

/** Visits views without asking their type: each view hands itself over, and the visitor asks it for what it needs. */
interface ViewVisitor {
  /** By default a window is visited by visiting each view placed in it. */
  default void visitWindow(Window window) {
    window.acceptChildren((view, position) -> view.accept(this));
  }

  default void visitButton(Button button) {
  }

  default void visitLabel(Label label) {
  }

  default void visitCheckBox(CheckBox box) {
  }

  default void visitInputLine(InputLine line) {
  }

  default void visitListBox(ListBox box) {
  }

  default void visitRadioButtons(RadioButtons group) {
  }
}
