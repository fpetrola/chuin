package com.fpetrola.tui;

/** Visits views without asking their type: each view hands itself over, and the visitor asks it for what it needs. */
interface ViewVisitor {
  /** A window is visited first, then each of its placements, in the order its views were added. */
  default void visitWindow(Window window) {
  }

  /** A view's place in its window is visited right before the view itself. */
  default void visitPlacement(Placement placement) {
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
