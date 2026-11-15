package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.text.view.TextCaption;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Option;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.List;
import java.util.function.IntConsumer;

/**
 * What Alt with a letter does in a window: the first view whose marked letter answers is reached. A button is focused
 * and pressed, a check box focused and toggled, an option's group focused and the option chosen, and a label passes
 * the focus to the view it is the label of. Until then it is searching; once one is reached, it meets the rest idly.
 */
class ReachByLetter implements ViewVisitor {
  /** A visitor that does nothing with what it meets: once a view is reached, nothing else is. */
  private static final ViewVisitor REACHED = new ViewVisitor() {
  };
  /** Where a view that cannot take the focus here is among the focusable ones. */
  private static final int NOWHERE = -1;
  private final char letter;
  private final List<View> focusable;
  private final IntConsumer focus;
  private ViewVisitor state = new Searching();

  ReachByLetter(char letter, List<View> focusable, IntConsumer focus) {
    this.letter = letter;
    this.focusable = focusable;
    this.focus = focus;
  }

  @Override
  public void visitButton(Button button) {
    state.visitButton(button);
  }

  @Override
  public void visitLabel(Label label) {
    state.visitLabel(label);
  }

  @Override
  public void visitCheckBox(CheckBox box) {
    state.visitCheckBox(box);
  }

  @Override
  public void visitOptionGroup(OptionGroup group) {
    state.visitOptionGroup(group);
  }

  @Override
  public void visitOption(Option option) {
    state.visitOption(option);
  }

  @Override
  public void visitChosenOption(Option option) {
    state.visitChosenOption(option);
  }

  /** Looks for the first view whose marked letter answers, and reaches it. */
  private class Searching implements ViewVisitor {
    @Override
    public void visitButton(Button button) {
      if (answers(button.caption())) {
        reach(focusable.indexOf(button));
        button.press();
      }
    }

    @Override
    public void visitLabel(Label label) {
      int linked = label.linked().map(focusable::indexOf).orElse(NOWHERE);
      if (linked != NOWHERE && answers(label.text())) {
        reach(linked);
      }
    }

    @Override
    public void visitCheckBox(CheckBox box) {
      if (answers(box.text())) {
        reach(focusable.indexOf(box));
        box.toggle();
      }
    }

    @Override
    public void visitOption(Option met) {
      choose(met);
    }

    @Override
    public void visitChosenOption(Option met) {
      choose(met);
    }

    private void choose(Option met) {
      if (answers(met.text())) {
        reach(focusable.indexOf(met.group()));
        met.choose();
      }
    }

    private boolean answers(String text) {
      return new TextCaption(text).answers(letter);
    }

    /** Focuses the view at this place among the focusable ones; from now on nothing else is reached. */
    private void reach(int place) {
      focus.accept(place);
      state = REACHED;
    }
  }
}
