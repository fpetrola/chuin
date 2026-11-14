package com.fpetrola.tui;

import java.util.function.BiConsumer;

class TextCheckBox implements CheckBox {
  private static final BiConsumer<ViewVisitor, CheckBox> CHECKED = ViewVisitor::visitChecked;
  private static final BiConsumer<ViewVisitor, CheckBox> UNCHECKED = ViewVisitor::visitUnchecked;
  private final String text;
  /** Whether it is checked, told without a flag: which visit of its mark it makes. */
  private BiConsumer<ViewVisitor, CheckBox> mark = UNCHECKED;

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
    mark.accept(visitor, this);
  }

  @Override
  public void toggle() {
    mark = mark == CHECKED ? UNCHECKED : CHECKED;
  }
}
