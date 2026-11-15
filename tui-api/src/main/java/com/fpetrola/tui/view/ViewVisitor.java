package com.fpetrola.tui.view;


/** Visits views without asking their type: each view hands itself over, and the visitor asks it for what it needs. */
public interface ViewVisitor {
  /** A window is visited first, then each of its placements, in the order its views were added. */
  default void visitWindow(Window window) {
  }

  /** A view's place in its window is visited right before the view itself. */
  default void visitPlacement(Placement placement) {
  }

  /** A scroll panel is entered before what it holds is visited. */
  default void visitScrollPanel(ScrollPanel panel) {
  }

  /** And left after it. */
  default void leaveScrollPanel(ScrollPanel panel) {
  }

  default void visitButton(Button button) {
  }

  /** A button held down is visited again after visitButton, before it is let go. */
  default void visitHeldButton(Button button) {
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

  default void visitScrollUp(ScrollBar bar) {
  }

  default void visitScrollTrack(ScrollBar bar) {
  }

  default void visitScrollThumb(ScrollBar bar) {
  }

  default void visitScrollDown(ScrollBar bar) {
  }

  /** Every item fits, so there is no scroll bar. */
  default void visitNoScrollBar(ScrollBar bar) {
  }

  /** A drop down is visited first, then, while its list is open, each of its items, the highlighted one apart. */
  default void visitDropDown(DropDown dropDown) {
  }

  default void visitDropDownItem(DropDownItem item) {
  }

  default void visitHighlightedDropDownItem(DropDownItem item) {
  }

  /** A group of options is visited first, then each of its options, top to bottom. */
  default void visitOptionGroup(OptionGroup group) {
  }

  default void visitOption(Option option) {
  }

  default void visitChosenOption(Option option) {
  }
}
