package com.fpetrola.tui;

import java.util.function.Consumer;

class TextCheckBox implements CheckBox {
  private static final Consumer<MarkVisitor> CHECKED = MarkVisitor::checked;
  private static final Consumer<MarkVisitor> UNCHECKED = MarkVisitor::unchecked;
  private final String text;
  /** Whether it is checked, told without a flag: which method of a MarkVisitor it calls. */
  private Consumer<MarkVisitor> mark = UNCHECKED;

  TextCheckBox(String text) {
    this.text = text;
  }

  @Override
  public String text() {
    return text;
  }

  @Override
  public void acceptMark(MarkVisitor visitor) {
    mark.accept(visitor);
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.visitCheckBox(this);
  }

  @Override
  public void toggle() {
    mark.accept(new MarkVisitor() {
      @Override
      public void checked() {
        mark = UNCHECKED;
      }

      @Override
      public void unchecked() {
        mark = CHECKED;
      }
    });
  }
}
