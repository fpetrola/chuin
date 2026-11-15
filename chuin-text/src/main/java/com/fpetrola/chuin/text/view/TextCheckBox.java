package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextSize;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.ViewVisitor;

import java.util.function.BiConsumer;

public class TextCheckBox implements CheckBox {
  /** The cells "[ ] " takes before the text. */
  private static final int BOX = 4;
  private final String text;
  private Mark mark = Mark.UNCHECKED;

  public TextCheckBox(String text) {
    this.text = text;
  }

  @Override
  public String text() {
    return text;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitCheckBox(this);
    mark.visit.accept(visitor, this);
  }

  @Override
  public void toggle() {
    mark = mark.opposite();
  }

  /** Whether a check box is checked, told without a flag: each mark knows its visit and the mark a toggle leads to. */
  private enum Mark {
    UNCHECKED(ViewVisitor::visitUnchecked),
    CHECKED(ViewVisitor::visitChecked);

    private final BiConsumer<ViewVisitor, CheckBox> visit;

    Mark(BiConsumer<ViewVisitor, CheckBox> visit) {
      this.visit = visit;
    }

    private Mark opposite() {
      return values()[(ordinal() + 1) % values().length];
    }
  }

  @Override
  public Size size() {
    return new TextSize(BOX + new TextCaption(text).shown().length(), 1);
  }
}
