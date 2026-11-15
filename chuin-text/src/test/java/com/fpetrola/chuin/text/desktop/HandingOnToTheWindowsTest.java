package com.fpetrola.chuin.text.desktop;

import com.fpetrola.chuin.TextToolkit;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.input.Key;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/** What the menus and the toolkit put in front of the windows, made from outside this package. */
class HandingOnToTheWindowsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final Windows windows = mock(Windows.class);

  @Test
  void aKeyNobodyElseKeepsGoesOnToTheWindows() {
    Key tab = toolkit.tabKey();

    tab.accept(new ToWindows(tab, windows));

    verify(windows).type(tab);
  }

  @Test
  void aDragReachesTheWindowsOnlyAfterAClickWentToThem() {
    Windows pointer = new PointerWindows(windows);

    pointer.drag(toolkit.point(3, 3));
    verify(windows, never()).drag(toolkit.point(3, 3));
    pointer.click(toolkit.point(1, 1));
    pointer.drag(toolkit.point(3, 3));

    verify(windows).drag(toolkit.point(3, 3));
  }

  @Test
  void aNewDesktopHasNoWindowSoEscapeRunsWhatElseItWasGiven() {
    Windows desktop = new TextDesktop();
    Runnable otherwise = mock(Runnable.class);

    desktop.escape(otherwise);

    verify(otherwise).run();
  }
}
