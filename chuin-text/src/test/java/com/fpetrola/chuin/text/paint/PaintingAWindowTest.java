package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.TextToolkit;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.text.pointer.ClickTarget;
import com.fpetrola.chuin.view.Window;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** A painter made from outside its package, as the desktop makes one for each window it draws. */
class PaintingAWindowTest {
  @Test
  void aPainterDrawsTheWindowItVisitsAndNotesWhereItsFocusableViewsWent() {
    Toolkit toolkit = new TextToolkit();
    Screen screen = toolkit.screen(20, 6);
    Window window = toolkit.window("Hi");
    window.place(toolkit.area(0, 0, 12, 5));
    window.add(toolkit.inputLine(5), toolkit.point(1, 1));
    List<Area> drawn = new ArrayList<>();
    List<Drawing> popups = new ArrayList<>();
    List<ClickTarget> targets = new ArrayList<>();

    window.accept(new Painter(screen, 0, drawn, Line.DOUBLE, popups, targets, held -> { }));

    assertTrue(screen.lines().get(0).contains(" Hi "));
    assertEquals(1, drawn.size());
    assertEquals(2, drawn.getFirst().column());
    assertEquals(2, drawn.getFirst().row());
  }
}
