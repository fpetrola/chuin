package com.fpetrola.tui;

import com.fpetrola.tui.screen.Screen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReadingAMarkedTextTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Screen screen = toolkit.screen(10, 1);

  @Test
  void aCaptionShowsItsTextWithoutTheMarksAndHighlightsTheMarkedLetter() {
    Caption caption = new TextCaption("Be~e~nden");

    caption.highlight(screen, 1, 0, TintRole.LETTER);

    assertEquals("Beenden", caption.shown());
    assertEquals("...h......", screen.tints().getFirst());
  }

  @Test
  void twoTildesAreOneTildeAndATextWithNoMarkHighlightsNothing() {
    Caption caption = new TextCaption("a~~b");

    caption.highlight(screen, 0, 0, TintRole.LETTER);

    assertEquals("a~b", caption.shown());
    assertEquals("..........", screen.tints().getFirst());
  }

  @Test
  void aLoneTildeAtTheEndIsShownAsItIs() {
    assertEquals("50~", new TextCaption("50~").shown());
  }

  @Test
  void aCaptionAnswersToItsMarkedLetterInEitherCaseAndToNoOther() {
    Caption marked = new TextCaption("Be~e~nden");
    Caption plain = new TextCaption("Beenden");

    assertEquals(true, marked.answers('e'));
    assertEquals(true, marked.answers('E'));
    assertEquals(false, marked.answers('b'));
    assertEquals(false, plain.answers('b'));
  }

  @Test
  void aMarkOfSeveralLettersIsHighlightedWholeAndAnswersToNoLetter() {
    Caption key = new TextCaption("~F3~ Laden");
    Caption odd = new TextCaption("~Ab~c");

    key.highlight(screen, 0, 0, TintRole.LETTER);

    assertEquals("F3 Laden", key.shown());
    assertEquals("hh........", screen.tints().getFirst());
    assertEquals(false, key.answers('f'));
    assertEquals(false, odd.answers('a'));
    assertEquals(false, odd.answers('b'));
  }

  @Test
  void theCaptionOfAKeyIsItsNameAndHasNoMarkedLetter() {
    Caption f3 = new KeyCaption(toolkit.functionKey(3));

    f3.highlight(screen, 0, 0, TintRole.LETTER);

    assertEquals("F3", f3.shown());
    assertEquals("Esc", new KeyCaption(toolkit.escapeKey()).shown());
    assertEquals(false, f3.answers('f'));
    assertEquals("..........", screen.tints().getFirst());
  }

  @Test
  void asAnEntryOfARowACaptionTakesItsShownTextAndABlankOnEachSide() {
    assertEquals(7, new TextCaption("~D~atei").entryWidth());
    assertEquals(10, new TextCaption("~F3~ Laden").entryWidth());
  }
}
