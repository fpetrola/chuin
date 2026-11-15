package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.geometry.TextSize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** The views the toolkit and the menus make for themselves, from outside this package: each starts as it was made. */
class MakingViewsTest {
  @Test
  void eachViewStartsWithWhatItWasMadeWith() {
    assertEquals("~O~K", new TextButton("~O~K").caption());
    assertEquals("~N~ame:", new TextLabel("~N~ame:", Optional.empty()).text());
    assertEquals("~S~ubscribe", new TextCheckBox("~S~ubscribe").text());
    assertEquals(12, new TextInputLine(12).size().columns());
    assertEquals("Notes", new TextWindow("Notes").title());
  }

  @Test
  void aListAndADropDownStartOnTheirFirstItem() {
    assertEquals(Optional.of("Apple"), new TextListBox(List.of("Apple", "Plum"), 3).selected());
    assertEquals(Optional.of("Small"), new TextDropDown(List.of("Small", "Large")).chosen());
    assertEquals(2, new TextOptionGroup(List.of("E-mail", "Phone")).size().rows());
  }

  @Test
  void aPanelIsAsBigAsItIsToldAndACursorStartsAtTheFirstPlace() {
    TextScrollPanel panel = new TextScrollPanel(new TextLabel("long", Optional.empty()), () -> new TextSize(3, 2));

    assertEquals(new TextSize(3, 2), panel.size());
    assertEquals(0, new TextCursor(4).at());
  }

  @Test
  void aCaptionShowsItsTextWithoutTheMarks() {
    assertEquals("Save", new TextCaption("~S~ave").shown());
  }
}
