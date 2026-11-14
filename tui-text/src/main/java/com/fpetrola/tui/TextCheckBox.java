package com.fpetrola.tui;

class TextCheckBox implements CheckBox {
  private static final Mark CHECKED = MarkVisitor::checked;
  private static final Mark UNCHECKED = MarkVisitor::unchecked;
  private final String text;
  private Mark mark = UNCHECKED;

  TextCheckBox(String text) {
    this.text = text;
  }

  @Override
  public void accept(ViewVisitor visitor) {
    visitor.checkBox(this, text, mark, this::toggle);
  }

  private void toggle() {
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
