package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

class LayingOutMenuTitlesTest {
  private final Toolkit toolkit = new TextToolkit();

  @Test
  void eachTitleStartsWhereTheOneBeforeItEndsWithASpaceOnBothSides() {
    MenuTitles titles = new BarTitles(bar());

    assertEquals(2, titles.count());
    assertEquals("~A~rchivo", titles.menu(1).title());
    assertEquals(0, titles.start(0));
    assertEquals(3, titles.start(1));
  }

  @Test
  void aCellOnTheTopRowFallsOnTheTitleThatCoversItAndALetterOnTheTitleThatAnswers() {
    MenuTitles titles = new BarTitles(bar());

    assertEquals(OptionalInt.of(0), titles.titleAt(new TextPoint(2, 0)));
    assertEquals(OptionalInt.of(1), titles.titleAt(new TextPoint(3, 0)));
    assertEquals(OptionalInt.empty(), titles.titleAt(new TextPoint(12, 0)));
    assertEquals(OptionalInt.empty(), titles.titleAt(new TextPoint(4, 1)));
    assertEquals(OptionalInt.of(1), titles.answering('a'));
    assertEquals(OptionalInt.empty(), titles.answering('z'));
  }

  @Test
  void aMenuHasARowForEachItemAndSeparatorButItemsOnlyForItems() {
    MenuTitles titles = new BarTitles(bar());

    List<Optional<MenuItem>> rows = titles.rows(1);
    List<MenuItem> items = titles.items(1);

    assertEquals(3, rows.size());
    assertEquals(Optional.empty(), rows.get(1));
    assertEquals(List.of("~G~uardar", "~S~alir"), items.stream().map(MenuItem::text).toList());
  }

  private MenuBar bar() {
    MenuBar bar = toolkit.menuBar();
    Menu system = toolkit.menu("≡");
    Menu file = toolkit.menu("~A~rchivo");
    file.add("~G~uardar", () -> { });
    file.separator();
    file.add("~S~alir", () -> { });
    bar.add(system);
    bar.add(file);
    return bar;
  }
}
