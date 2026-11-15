package com.fpetrola.chuin;

import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.DropDownItem;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.ListItem;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.ScrollBar;
import com.fpetrola.chuin.view.ViewVisitor;



/** Counts a window's focusable views and hands only the focused one to an action. */
class AtFocus implements ViewVisitor {
  /** A visitor that does nothing with what it meets. */
  private static final ViewVisitor NOWHERE = new ViewVisitor() {
  };
  private final FocusOrder order;
  private final ViewVisitor action;
  /** Where the parts of the view met last go, a drop down's items, a group's options or a list's rows and scroll bar: on to the action when it is the focused one, nowhere otherwise. */
  private ViewVisitor parts = NOWHERE;

  AtFocus(int focused, ViewVisitor action) {
    this.order = new TextFocusOrder(focused);
    this.action = action;
  }

  @Override
  public void visitButton(Button button) {
    if (reach()) {
      action.visitButton(button);
    }
  }

  @Override
  public void visitHeldButton(Button button) {
    parts.visitHeldButton(button);
  }

  @Override
  public void visitLabel(Label label) {
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    if (order.next()) {
      action.visitCheckBox(box);
    }
  }

  @Override
  public void visitInputLine(InputLine line) {
    if (order.next()) {
      action.visitInputLine(line);
    }
  }

  @Override
  public void visitListBox(ListBox box) {
    if (reach()) {
      action.visitListBox(box);
    }
  }

  @Override
  public void visitOptionGroup(OptionGroup group) {
    if (reach()) {
      action.visitOptionGroup(group);
    }
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    if (reach()) {
      action.visitDropDown(dropDown);
    }
  }

  /** The items of the focused drop down's open list go on to the action too. */
  @Override
  public void visitDropDownItem(DropDownItem item) {
    parts.visitDropDownItem(item);
  }

  @Override
  public void visitHighlightedDropDownItem(DropDownItem item) {
    parts.visitHighlightedDropDownItem(item);
  }

  /** The cells of the focused list's scroll bar go on to the action too. */
  @Override
  public void visitScrollUp(ScrollBar bar) {
    parts.visitScrollUp(bar);
  }

  @Override
  public void visitScrollTrack(ScrollBar bar) {
    parts.visitScrollTrack(bar);
  }

  @Override
  public void visitScrollThumb(ScrollBar bar) {
    parts.visitScrollThumb(bar);
  }

  @Override
  public void visitScrollDown(ScrollBar bar) {
    parts.visitScrollDown(bar);
  }

  @Override
  public void visitNoScrollBar(ScrollBar bar) {
    parts.visitNoScrollBar(bar);
  }

  /** The options of the focused group and the rows of the focused list go on to the action too. */
  @Override
  public void visitOption(Option option) {
    parts.visitOption(option);
  }

  @Override
  public void visitChosenOption(Option option) {
    parts.visitChosenOption(option);
  }

  @Override
  public void visitListItem(ListItem item) {
    parts.visitListItem(item);
  }

  @Override
  public void visitSelectedListItem(ListItem item) {
    parts.visitSelectedListItem(item);
  }

  /** Counts the focusable view just met; its parts go on to the action only when it is the focused one. */
  private boolean reach() {
    boolean onFocused = order.next();
    parts = onFocused ? action : NOWHERE;
    return onFocused;
  }
}
