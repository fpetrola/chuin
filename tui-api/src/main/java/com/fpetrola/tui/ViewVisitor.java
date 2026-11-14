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

  /** A check box is visited first, then its mark: checked or unchecked. */
  default void visitCheckBox(CheckBox box) {
  }

  default void visitChecked(CheckBox box) {
  }

  default void visitUnchecked(CheckBox box) {
  }

  default void visitInputLine(InputLine line) {
  }

  /** A list box is visited first, then each row it shows, top to bottom: its item, then its cell of the scroll bar. */
  default void visitListBox(ListBox box) {
  }

  default void visitListItem(ListItem item) {
  }

  default void visitSelectedListItem(ListItem item) {
  }

  /** A row below the last item. */
  default void visitNoListItem(ListBox box) {
  }

  default void visitScrollUp(ListBox box) {
  }

  default void visitScrollTrack(ListBox box) {
  }

  default void visitScrollThumb(ListBox box) {
  }

  default void visitScrollDown(ListBox box) {
  }

  /** Every item fits, so there is no scroll bar. */
  default void visitNoScrollBar(ListBox box) {
  }

  default void visitRadioButtons(RadioButtons group) {
  }
}
