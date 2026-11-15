package com.fpetrola.tui.demo;

import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.view.Window;
import java.util.List;

/** A window smaller than what it holds: scroll bars appear on its frame, and the arrows, the mouse or the focus move it. */
class ScrollingWindow implements Example {
  private static final List<String> POEM = List.of(
      "The Road Not Taken, by Robert Frost (1916)",
      "",
      "Two roads diverged in a yellow wood,",
      "And sorry I could not travel both",
      "And be one traveler, long I stood",
      "And looked down one as far as I could",
      "To where it bent in the undergrowth;",
      "",
      "Then took the other, as just as fair,",
      "And having perhaps the better claim,",
      "Because it was grassy and wanted wear;",
      "Though as for that the passing there",
      "Had worn them really about the same,",
      "",
      "And both that morning equally lay",
      "In leaves no step had trodden black.",
      "Oh, I kept the first for another day!",
      "Yet knowing how way leads on to way,",
      "I doubted if I should ever come back.");

  @Override
  public String name() {
    return "scrolling";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Window window = toolkit.window("A poem");
    window.place(toolkit.area(16, 4, 34, 13));
    for (int line = 0; line < POEM.size(); line++) {
      window.add(toolkit.label(POEM.get(line)), toolkit.point(1, line));
    }
    window.add(toolkit.button("~D~one"), toolkit.point(1, POEM.size() + 1));
    desktop.open(window);
  }
}
