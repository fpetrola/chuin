package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.geometry.Size;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/** Each view tells how many columns and rows it takes where it is placed, as it is drawn. */
class SizingViewsTest {
  /** What a button's shadow is drawn with, over what is around it. */
  private static final List<String> SHADOW = List.of("▄", "▀");
  private final Toolkit toolkit = new TextToolkit();

  @Test
  void aButtonTakesItsFaceAndItsShadowTakesNoRoom() {
    assertEquals("6x1", of(toolkit.button("~O~k")));
  }

  @Test
  void aLabelTakesTheTextItShowsOnOneRow() {
    assertEquals("6x1", of(toolkit.label("~N~ombre")));
  }

  @Test
  void aCheckBoxTakesItsBoxAndItsText() {
    assertEquals("9x1", of(toolkit.checkBox("~S~exto")));
  }

  @Test
  void anInputLineTakesItsWidthOrItsTextWhenLonger() {
    InputLine line = toolkit.inputLine(3);
    assertEquals("3x1", of(line));
    line.type('a');
    line.type('b');
    line.type('c');
    line.type('d');
    assertEquals("4x1", of(line));
  }

  @Test
  void aListBoxTakesItsWidthByItsRowsAndAGroupItsWidthByItsOptions() {
    assertEquals("8x4", of(toolkit.listBox(List.of("uno", "cuatro", "seis", "x", "y"), 4)));
    assertEquals("9x3", of(toolkit.optionGroup(List.of("~S~i", "~N~o", "Nunca"))));
  }

  @Test
  void aDropDownTakesItsFieldAndArrowWithoutItsList() {
    assertEquals("11x1", of(toolkit.dropDown(List.of("Ja", "Vielleicht"))));
  }

  /**
   * The size is a promise about the drawing: each kind of view, drawn alone in a window that is not on top, changes
   * exactly the cells its size says from where it is placed, by what is written or by its tint.
   */
  @Test
  void eachKindOfViewDrawsExactlyItsSize() {
    InputLine typed = toolkit.inputLine(3);
    typed.type('a');
    typed.type('b');
    typed.type('c');
    typed.type('d');
    List<View> views = List.of(
        toolkit.button("~O~k"),
        toolkit.label("~N~ombre"),
        toolkit.checkBox("~S~exto"),
        toolkit.inputLine(5),
        typed,
        toolkit.listBox(List.of("uno", "cuatro", "seis", "x", "y"), 4),
        toolkit.listBox(List.of("uno", "dos"), 3),
        toolkit.optionGroup(List.of("~S~i", "~N~o", "Nunca")),
        toolkit.dropDown(List.of("Ja", "Vielleicht")));
    for (View view : views) {
      assertEquals(of(view), drawn(view), view.getClass().getSimpleName());
    }
  }

  /**
   * Draws the view alone at 2, 1 inside a window under another, and gives the columns and rows its drawing wrote or
   * tinted, blanks included. Clearing the screen and drawing the frame write whole rows from the first column, and the
   * window's shadow falls past its right side; what a view draws starts inside the frame. A button's shadow, like the
   * window's, falls on what is around it and is not counted.
   */
  private static String drawn(View view) {
    Toolkit own = new TextToolkit();
    Screen screen = own.screen(30, 10);
    List<int[]> cells = new java.util.ArrayList<>();
    Screen recording = new Screen() {
      @Override
      public List<String> lines() {
        return screen.lines();
      }

      @Override
      public void write(int column, int row, String text) {
        if (!SHADOW.contains(text)) {
          record(column, row, text.length());
        }
        screen.write(column, row, text);
      }

      @Override
      public List<String> tints() {
        return screen.tints();
      }

      @Override
      public void paint(int column, int row, int columns, char tint) {
        record(column, row, columns);
        screen.paint(column, row, columns, tint);
      }

      @Override
      public void showCursorAt(Point cell) {
        screen.showCursorAt(cell);
      }

      @Override
      public void hideCursor() {
        screen.hideCursor();
      }

      @Override
      public java.util.Optional<Point> cursor() {
        return screen.cursor();
      }

      private void record(int column, int row, int length) {
        for (int at = column; at < column + length && column > 0 && row > 0 && row < 9; at++) {
          if (at < 29) {
            cells.add(new int[] {at, row});
          }
        }
      }
    };
    Desktop desktop = own.desktop();
    Window window = own.window("");
    Window above = own.window("");
    window.place(own.area(0, 0, 30, 10));
    window.add(view, own.point(2, 1));
    above.place(own.area(40, 0, 5, 5));
    desktop.open(window);
    desktop.open(above);
    desktop.draw(recording);
    int left = cells.stream().mapToInt(cell -> cell[0]).min().orElseThrow();
    int top = cells.stream().mapToInt(cell -> cell[1]).min().orElseThrow();
    int right = cells.stream().mapToInt(cell -> cell[0]).max().orElseThrow();
    int bottom = cells.stream().mapToInt(cell -> cell[1]).max().orElseThrow();
    assertEquals(3, left, "where it starts across");
    assertEquals(2, top, "where it starts down");
    return (right - left + 1) + "x" + (bottom - top + 1);
  }

  private static String of(View view) {
    Size size = view.size();
    return size.columns() + "x" + size.rows();
  }
}
