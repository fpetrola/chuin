package com.fpetrola.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ReadingAMarkedTextTest {
  private final Screen screen = new TextToolkit().screen(10, 1);

  @Test
  void aCaptionShowsItsTextWithoutTheMarksAndHighlightsTheMarkedLetter() {
    Caption caption = new TextCaption("Be~e~nden");

    caption.highlight(screen, 1, 0, 'h');

    assertEquals("Beenden", caption.shown());
    assertEquals("...h......", screen.tints().getFirst());
  }

  @Test
  void twoTildesAreOneTildeAndATextWithNoMarkHighlightsNothing() {
    Caption caption = new TextCaption("a~~b");

    caption.highlight(screen, 0, 0, 'h');

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

    key.highlight(screen, 0, 0, 'h');

    assertEquals("F3 Laden", key.shown());
    assertEquals("hh........", screen.tints().getFirst());
    assertEquals(false, key.answers('f'));
    assertEquals(false, odd.answers('a'));
    assertEquals(false, odd.answers('b'));
  }

  @Test
  void theCaptionOfAKeyIsItsNameAndHasNoMarkedLetter() {
    Keys keys = new TextToolkit().keys();
    Caption f3 = new KeyCaption(keys.function(3));

    f3.highlight(screen, 0, 0, 'h');

    assertEquals("F3", f3.shown());
    assertEquals("Esc", new KeyCaption(keys.escape()).shown());
    assertEquals(false, f3.answers('f'));
    assertEquals("..........", screen.tints().getFirst());
  }
}
