package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

/** A menu shows each item's shortcut aligned on the right, and separators the arrows step over. */
class SeeingAFullMenuTest {
  private static final String F10 = "\u001b[21~";
  private static final String DOWN = "\u001b[B";
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(24, 8);
  private final Desktop desktop = toolkit.desktop();
  private final Label said = toolkit.label("");

  @Test
  void anOpenMenuShowsTheNameOfEachItemsKeyOnTheRightAndASeparatorLine() {
    open();
    press(F10);
    desktop.draw(screen);

    assertEquals(List.of(
        "┌──────────────┐",
        "│ Neu          │",
        "│ Laden     F3 │",
        "├──────────────┤",
        "│ Beenden  Esc │",
        "└──────────────┘"), screen.lines().subList(1, 7).stream().map(line -> line.substring(0, 16)).toList());
  }

  @Test
  void theArrowsStepOverASeparator() {
    open();
    press(F10 + DOWN + DOWN + "\r");

    assertEquals("beenden", said.text());
  }

  @Test
  void theKeyOfAnItemRunsItFromAnywhereWithoutOpeningTheMenu() {
    open();
    press("\u001bOR");
    desktop.draw(screen);

    assertEquals("laden", said.text());
    assertEquals("                        ", screen.lines().get(1));
  }

  @Test
  void aMenuIsVisitedThenItsItemsWithTheirShortcutsAndItsSeparatorsInOrder() {
    Menu file = toolkit.menu("Datei");
    file.add("Laden", toolkit.keys().function(3), () -> { });
    file.separator();
    MenuVisitor visitor = org.mockito.Mockito.mock(MenuVisitor.class);
    org.mockito.ArgumentCaptor<MenuItem> item = org.mockito.ArgumentCaptor.captor();

    file.accept(visitor);

    org.mockito.InOrder order = org.mockito.Mockito.inOrder(visitor);
    order.verify(visitor).visitMenu(file);
    order.verify(visitor).visitMenuItem(item.capture());
    order.verify(visitor).visitMenuSeparator(file);
    List<String> named = new java.util.ArrayList<>();
    item.getValue().key().orElseThrow().accept(new KeyVisitor() {
      @Override
      public void function(int number) {
        named.add("F" + number);
      }
    });
    assertEquals(List.of("F3"), named);
  }

  private void open() {
    MenuBar bar = toolkit.menuBar();
    Menu file = toolkit.menu("Datei");
    file.add("Neu", () -> said.text("neu"));
    file.add("Laden", toolkit.keys().function(3), () -> said.text("laden"));
    file.separator();
    file.add("Beenden", toolkit.keys().escape(), () -> said.text("beenden"));
    bar.add(file);
    desktop.show(bar);
  }

  private void press(String typed) {
    toolkit.keyboard(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8))).typeInto(desktop);
  }
}
