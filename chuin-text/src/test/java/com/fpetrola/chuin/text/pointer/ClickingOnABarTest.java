package com.fpetrola.chuin.text.pointer;

import com.fpetrola.chuin.text.geometry.TextPoint;
import com.fpetrola.chuin.view.ScrollBar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/** A click on a cell of a scroll bar, made from outside this package, as a list's click makes it. */
class ClickingOnABarTest {
  private final ScrollBar bar = mock(ScrollBar.class);
  private final BarLine down = new TextBarLine(new TextPoint(0, 0), new TextPoint(0, 1));

  /** Walks a bar of five cells, ▲, track, thumb, track, ▼, as the bar itself walks them for a visitor. */
  private void walk(BarClick click) {
    click.visitScrollUp(bar);
    click.visitScrollTrack(bar);
    click.visitScrollThumb(bar);
    click.visitScrollTrack(bar);
    click.visitScrollDown(bar);
  }

  @Test
  void aClickOnTheLastCellScrollsAStepForwardOnceTheWalkIsOver() {
    Settle pending = new PendingAction(() -> { });

    walk(new BarClick(4, pending, down, held -> { }));
    verify(bar, never()).next();
    pending.settle();

    verify(bar).next();
  }

  @Test
  void aClickOnTheTrackPastTheThumbScrollsAPageForward() {
    Settle pending = new PendingAction(() -> { });

    walk(new BarClick(3, pending, down, held -> { }));
    pending.settle();

    verify(bar).pageDown();
    verify(bar, never()).pageUp();
  }
}
