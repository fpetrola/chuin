package com.fpetrola.chuin.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * What a terminal would show after reading ANSI output, kept as cells with their colours, and drawn as a PNG picture:
 * the screenshots of the README are made this way, from what the toolkit writes, with no terminal involved.
 */
class AnsiPicture {
  private static final Pattern SEQUENCE = Pattern.compile("\u001b\\[([0-9;?]*)([a-zA-Z])");
  private static final int FONT_SIZE = 16;
  private final int columns;
  private final int rows;
  private final char[][] text;
  private final String[][] foreground;
  private final String[][] background;

  AnsiPicture(int columns, int rows, String ansi) {
    this.columns = columns;
    this.rows = rows;
    this.text = new char[rows][columns];
    this.foreground = new String[rows][columns];
    this.background = new String[rows][columns];
    read(ansi);
  }

  /** Follows the output: moves the cursor, takes colours, and writes characters where the cursor is. */
  private void read(String ansi) {
    Matcher matcher = SEQUENCE.matcher(ansi);
    int row = 0;
    int column = 0;
    String fore = "#a8a8a8";
    String back = "#000000";
    int at = 0;
    while (at < ansi.length()) {
      if (matcher.find(at) && matcher.start() == at) {
        String arguments = matcher.group(1);
        String command = matcher.group(2);
        if (command.equals("H") && !arguments.isEmpty()) {
          String[] place = arguments.split(";");
          row = Integer.parseInt(place[0]) - 1;
          column = Integer.parseInt(place[1]) - 1;
        } else if (command.equals("m")) {
          String[] colours = colours(arguments, fore, back);
          fore = colours[0];
          back = colours[1];
        }
        at = matcher.end();
      } else {
        int point = ansi.codePointAt(at);
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
          text[row][column] = (char) point;
          foreground[row][column] = fore;
          background[row][column] = back;
        }
        column++;
        at += Character.charCount(point);
      }
    }
  }

  /** The foreground and background after a colour sequence: 24-bit ones, as the toolkit writes with COLORTERM set. */
  private static String[] colours(String arguments, String fore, String back) {
    String[] codes = arguments.split(";");
    String[] chosen = {fore, back};
    for (int at = 0; at < codes.length; at++) {
      boolean rgb = at + 4 < codes.length && codes[at + 1].equals("2");
      if (rgb && (codes[at].equals("38") || codes[at].equals("48"))) {
        String colour = String.format("#%02x%02x%02x", Integer.parseInt(codes[at + 2]), Integer.parseInt(codes[at + 3]),
            Integer.parseInt(codes[at + 4]));
        chosen[codes[at].equals("38") ? 0 : 1] = colour;
        at += 4;
      }
    }
    return chosen;
  }

  /** The picture as a PNG image: each cell its background, and its character over it in its foreground. */
  void png(Path file) throws IOException {
    Font font = new Font("DejaVu Sans Mono", Font.PLAIN, FONT_SIZE);
    BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    FontMetrics metrics = probe.createGraphics().getFontMetrics(font);
    int width = metrics.charWidth('M');
    int height = metrics.getAscent() + metrics.getDescent();
    BufferedImage image = new BufferedImage(columns * width, rows * height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    graphics.setFont(font);
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        graphics.setColor(Color.decode(or(background[row][column], "#000000")));
        graphics.fillRect(column * width, row * height, width, height);
        char cell = text[row][column];
        if (cell != 0 && cell != ' ') {
          graphics.setColor(Color.decode(or(foreground[row][column], "#a8a8a8")));
          graphics.drawString(String.valueOf(cell), column * width, row * height + metrics.getAscent());
        }
      }
    }
    graphics.dispose();
    ImageIO.write(image, "png", file.toFile());
  }

  private static boolean same(String one, String other) {
    return one == null ? other == null : one.equals(other);
  }

  private static String or(String colour, String otherwise) {
    return colour == null ? otherwise : colour;
  }
}
