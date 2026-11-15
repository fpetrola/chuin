package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.view.Button;
import com.fpetrola.tui.view.CheckBox;
import com.fpetrola.tui.view.DropDown;
import com.fpetrola.tui.view.InputLine;
import com.fpetrola.tui.view.Label;
import com.fpetrola.tui.view.ListBox;
import com.fpetrola.tui.view.OptionGroup;
import com.fpetrola.tui.view.Placement;
import com.fpetrola.tui.view.View;
import com.fpetrola.tui.view.ViewVisitor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Collects the views that can take the focus, in the order they are visited, and where each is placed and how big. */
class CollectFocusable implements ViewVisitor {
  /** What is done with each view that can take the focus. */
  private final Consumer<View> found;
  /** Where the view met next is placed. */
  private Area placed;

  CollectFocusable(List<View> into) {
    this.found = into::add;
  }

  /** Collects each such view with where it is placed and how big it is, keeping them in the order they are visited. */
  CollectFocusable(Map<View, Area> into) {
    this.found = view -> into.put(view, placed);
  }

  @Override
  public void visitPlacement(Placement placement) {
    placed = placement.area();
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
