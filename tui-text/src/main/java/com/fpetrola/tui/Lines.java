package com.fpetrola.tui;

/** Single lines for most frames, double lines for the window on top. */
enum Lines implements Border {
  SINGLE("┌┐└┘─│"),
  DOUBLE("╔╗╚╝═║");

  /** Corners top left, top right, bottom left, bottom right, then the line across and the line down. */
  private final String drawn;

  Lines(String drawn) {
    this.drawn = drawn;
  }

  /** A title wider than the room inside is left off, and the line runs across. */
  @Override
  public String top(String title, int inner) {
    String label = title.length() > inner ? "" : title;
    int left = (inner - label.length()) / 2;
    return drawn.charAt(0) + across(left) + label + across(inner - label.length() - left) + drawn.charAt(1);
  }

  @Override
  public String middle(int inner) {
    return drawn.charAt(5) + " ".repeat(inner) + drawn.charAt(5);
  }

  @Override
  public String bottom(int inner) {
    return drawn.charAt(2) + across(inner) + drawn.charAt(3);
  }

  private String across(int cells) {
    return String.valueOf(drawn.charAt(4)).repeat(cells);
  }
}
