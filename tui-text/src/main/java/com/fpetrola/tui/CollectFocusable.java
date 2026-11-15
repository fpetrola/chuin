package com.fpetrola.tui;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Collects the views that can take the focus, in the order they are visited, and where each is placed in its window. */
class CollectFocusable implements ViewVisitor {
  /** What is done with each view that can take the focus. */
  private final Consumer<View> found;
  /** Where the view met next is placed. */
  private Point placed;

  CollectFocusable(List<View> into) {
    this.found = into::add;
  }

  /** Collects each such view with where it is placed, keeping them in the order they are visited. */
  CollectFocusable(Map<View, Point> into) {
    this.found = view -> into.put(view, placed);
  }

  @Override
  public void visitPlacement(Placement placement) {
    placed = placement.position();
  }

  @Override
  public void visitButton(Button button) {
    add(button);
  }

  @Override
  public void visitLabel(Label label) {
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    add(box);
  }

  @Override
  public void visitInputLine(InputLine line) {
    add(line);
  }

  @Override
  public void visitListBox(ListBox box) {
    add(box);
  }

  @Override
  public void visitOptionGroup(OptionGroup group) {
    add(group);
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    add(dropDown);
  }

  private void add(View view) {
    found.accept(view);
  }
}
