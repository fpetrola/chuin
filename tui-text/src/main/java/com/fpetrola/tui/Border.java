package com.fpetrola.tui;

/** The lines a frame is drawn with: single for most, double for the window on top. */
enum Border {
  SINGLE('┌', '┐', '└', '┘', '─', '│'),
  DOUBLE('╔', '╗', '╚', '╝', '═', '║');

  final char topLeft;
  final char topRight;
  final char bottomLeft;
  final char bottomRight;
  final char across;
  final char down;

  Border(char topLeft, char topRight, char bottomLeft, char bottomRight, char across, char down) {
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
    this.across = across;
    this.down = down;
  }
}
