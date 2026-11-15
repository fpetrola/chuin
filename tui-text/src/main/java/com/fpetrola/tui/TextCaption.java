package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;


/** Reads a marked text once: what is shown, and where in it the marked letter or letters are. */
class TextCaption implements Caption {
  private static final char MARK = '~';
  private static final int NONE = -1;
  private final String shown;
  private final int marked;
  /** How many letters are marked: one answers to Alt, several are only highlighted. */
  private final int length;

  TextCaption(String text) {
    StringBuilder visible = new StringBuilder();
    int start = NONE;
    int length = 0;
    for (int at = 0; at < text.length(); at++) {
      char current = text.charAt(at);
      boolean doubled = current == MARK && at + 1 < text.length() && text.charAt(at + 1) == MARK;
      int closing = current == MARK && !doubled ? text.indexOf(MARK, at + 2) : NONE;
      if (closing != NONE) {
        start = visible.length();
        length = closing - at - 1;
        visible.append(text, at + 1, closing);
        at = closing;
      } else {
        visible.append(current);
        at += doubled ? 1 : 0;
      }
    }
    this.shown = visible.toString();
    this.marked = start;
    this.length = length;
  }


  @Override
  public String shown() {
    return shown;
  }

  @Override
  public void highlight(Screen screen, int column, int row, char tint) {
    if (marked != NONE) {
      screen.paint(column + marked, row, length, tint);
    }
  }

  @Override
  public boolean answers(char letter) {
    return length == 1 && Character.toLowerCase(shown.charAt(marked)) == Character.toLowerCase(letter);
  }
}
