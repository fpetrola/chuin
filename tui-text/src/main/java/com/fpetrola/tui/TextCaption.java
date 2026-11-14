package com.fpetrola.tui;

/** Reads a marked text once: what is shown, and where in it the marked letter is. */
class TextCaption implements Caption {
  private static final char MARK = '~';
  private static final int NONE = -1;
  private final String shown;
  private final int marked;

  TextCaption(String text) {
    StringBuilder visible = new StringBuilder();
    int letter = NONE;
    for (int at = 0; at < text.length(); at++) {
      char current = text.charAt(at);
      boolean doubled = current == MARK && at + 1 < text.length() && text.charAt(at + 1) == MARK;
      boolean opening = current == MARK && !doubled && at + 2 < text.length() && text.charAt(at + 2) == MARK;
      if (opening) {
        letter = visible.length();
        visible.append(text.charAt(at + 1));
        at += 2;
      } else {
        visible.append(current);
        at += doubled ? 1 : 0;
      }
    }
    this.shown = visible.toString();
    this.marked = letter;
  }

  @Override
  public String shown() {
    return shown;
  }

  @Override
  public void highlight(Screen screen, int column, int row, char tint) {
    if (marked != NONE) {
      screen.paint(column + marked, row, 1, tint);
    }
  }

  @Override
  public boolean answers(char letter) {
    return marked != NONE && Character.toLowerCase(shown.charAt(marked)) == Character.toLowerCase(letter);
  }
}
