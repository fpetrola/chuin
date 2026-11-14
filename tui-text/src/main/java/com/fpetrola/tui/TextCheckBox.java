package com.fpetrola.tui;

import java.util.function.BiConsumer;

class TextCheckBox implements CheckBox {
  private final String text;
  private Mark mark = Mark.UNCHECKED;

  TextCheckBox(String text) {
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
}
