package com.fpetrola.chuin.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.TextToolkit;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.screen.Screen;
import java.util.List;
import org.junit.jupiter.api.Test;

class BuildingTheExamplesTest {
  @Test
  void anExampleIsKnownByItsNameAndPutsItsWindowOnTheDesktop() {
    Example hello = new HelloWorld();
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();

    hello.build(toolkit, desktop);
    Screen screen = toolkit.screen(80, 25);
    desktop.draw(screen);

    assertEquals("hello", hello.name());
    assertTrue(screen.lines().get(9).contains("Hello, world!"), String.join("\n", screen.lines()));
  }

  @Test
  void everyExampleHasANameOfItsOwn() {
    List<Example> all = List.of(new AddressBook(), new Inventory(), new HelloWorld(), new FormControls(),
        new ListsAndDropDowns(), new MenusAndStatusLine(), new ScrollingWindow());

    assertEquals(all.size(), all.stream().map(Example::name).distinct().count());
  }
}
