package com.fpetrola.tui;

/**
 * What a left click does to the view under it, once focused: presses a button, toggles a check box, chooses the option
 * on the row clicked, opens a drop down's list; an input line is only focused. On a list, the click selects the item on its row, or, on the
 * scroll bar, the item before (▲) or after (▼), or a page before or after (the track above or below the thumb). Every
 * action is only noted during the visit, and done once the walk is over.
 */
class ClickFocused implements ViewVisitor {
  /** What the click or key does once the walk is over. */
  private final Settle pending;
  private static final Runnable NOTHING = () -> { };
  /** Which column and row of the view was clicked, counted from its first. */
  private final int column;
  private final int row;
  /** The row of the scroll bar the walk is on. */
  private int walked;
  /** Whether the walk has met the thumb, so a track cell is below it. */
  private boolean pastThumb;

  ClickFocused(int column, int row, Settle pending) {
    this.pending = pending;
    this.column = column;
    this.row = row;
  }

  @Override
  public void visitButton(Button button) {
    pending.note(button::press);
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    pending.note(box::toggle);
  }

  /** An item is selected wherever its row is clicked, unless the bar's column takes the click. */
  @Override
  public void visitListBox(ListBox box) {
    pending.note(() -> box.selectShown(row));
  }

  @Override
  public void visitScrollUp(ListBox box) {
    onBar(box, box::previous);
  }

  @Override
  public void visitScrollTrack(ListBox box) {
    onBar(box, pastThumb ? box::pageDown : box::pageUp);
  }

  @Override
  public void visitScrollThumb(ListBox box) {
    pastThumb = true;
    onBar(box, NOTHING);
  }

  @Override
  public void visitScrollDown(ListBox box) {
    onBar(box, box::next);
  }

  @Override
  public void visitNoScrollBar(ListBox box) {
    walked++;
  }

  @Override
  public void visitDropDown(DropDown dropDown) {
    pending.note(dropDown::down);
  }

  @Override
  public void visitRadioButtons(RadioButtons group) {
    pending.note(() -> group.choose(row));
  }


  /** Notes what the bar's cell on this row does, when it is the one clicked. */
  private void onBar(ListBox box, Runnable does) {
    if (walked++ == row && column == box.width() - 1) {
      pending.note(does);
    }
  }
}
