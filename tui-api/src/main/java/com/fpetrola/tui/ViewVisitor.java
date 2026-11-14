package com.fpetrola.tui;

/** Visits views without asking their type: each view hands itself over, and the visitor asks it for what it needs. */
interface ViewVisitor {
  /** Whether to go into the views placed in the window is the visitor's call: see Window#acceptViews. */
  default void visitWindow(Window window) {
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
