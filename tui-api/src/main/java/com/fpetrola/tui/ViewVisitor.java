package com.fpetrola.tui;


/** Visits views without asking their type: each view hands over what it holds. */
interface ViewVisitor {
  default void window(String title, Area area) {
  }

  /** The next view visited sits at this position inside the window being visited. */
  default void child(Point position) {
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
