package com.fpetrola.tui;

/**
 * What a left click does to the view under it, once focused: presses a button, toggles a check box, chooses the option
 * on the row clicked; an input line is only focused. On a list, a click on an item selects it; on its scroll bar, ▲ and
 * ▼ select the item before or after, and the track above or below the thumb moves the selection a page, once the walk
 * reaches ▼ and the page is known.
 */
class ClickFocused implements ViewVisitor {
  /** Which column and row of the view was clicked, counted from its first. */
  private final int column;
  private final int row;
  /** The row of the scroll bar the walk is on. */
  private int walked;
  private boolean pastThumb;
  /** Whether the click fell on the list's last column, where its scroll bar is. */
  private boolean onBar;
  /** How the selection moves a page, decided at the track and done at ▼. */
  private int page;

  ClickFocused(int column, int row) {
    this.column = column;
    this.row = row;
  }

  @Override
  public void visitButton(Button button) {
    button.press();
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    box.toggle();
  }

  @Override
  public void visitListBox(ListBox box) {
    onBar = column == box.width() - 1;
    if (!onBar) {
      box.selectShown(row);
    }
  }

  @Override
  public void visitNoScrollBar(ListBox box) {
    if (onClicked()) {
      box.selectShown(row);
    }
    walked++;
  }

  @Override
  public void visitScrollUp(ListBox box) {
    if (onClicked()) {
      box.previous();
    }
    walked++;
  }

  @Override
  public void visitScrollTrack(ListBox box) {
    if (onClicked()) {
      page = pastThumb ? 1 : -1;
    }
    walked++;
  }

  @Override
  public void visitScrollThumb(ListBox box) {
    pastThumb = true;
    walked++;
  }

  @Override
  public void visitScrollDown(ListBox box) {
    for (int step = 0; step < walked; step++) {
      move(box, page);
    }
    if (onClicked()) {
      box.next();
    }
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    group.choose(row);
  }

  private boolean onClicked() {
    return onBar && walked == row;
  }

  private static void move(ListBox box, int toward) {
    if (toward > 0) {
      box.next();
    } else if (toward < 0) {
      box.previous();
    }
  }
}
