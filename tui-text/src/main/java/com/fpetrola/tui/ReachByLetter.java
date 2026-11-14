package com.fpetrola.tui;

import java.util.List;
import java.util.function.IntConsumer;

/**
 * What Alt with a letter does in a window: the first button whose caption answers to it is focused and pressed, or
 * the first label that answers passes the focus to the view it is the label of.
 */
class ReachByLetter implements ViewVisitor {
  private final char letter;
  private final List<View> focusable;
  private final IntConsumer focus;
  private boolean reached;

  ReachByLetter(char letter, List<View> focusable, IntConsumer focus) {
    this.letter = letter;
    this.focusable = focusable;
    this.focus = focus;
  }

  @Override
  public void visitButton(Button button) {
    if (!reached && new TextCaption(button.caption()).answers(letter)) {
      reached = true;
      focus.accept(focusable.indexOf(button));
      button.press();
    }
  }

  @Override
  public void visitLabel(Label label) {
    if (!reached && new TextCaption(label.text()).answers(letter) && label.linked().isPresent()) {
      reached = true;
      focus.accept(focusable.indexOf(label.linked().get()));
    }
  }
}
