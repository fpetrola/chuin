package com.fpetrola.chuin.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.TextToolkit;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.screen.Screen;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/**
 * Draws each example as the README shows it, on an 80 by 25 screen in the DOS colours, and checks the picture has it.
 * Run with {@code -Dscreenshots=docs/screenshots} (a path from the chuin-demo module) to write the pictures there.
 */
class TakingScreenshotsTest {
  private static final int COLUMNS = 80;
  private static final int ROWS = 25;

  @Test
  void theAddressBook() throws IOException {
    take(new AddressBook(), desktop -> { }, "address-book", "Address database");
  }

  @Test
  void theAddressBookWithItsSearchMenuOpen() throws IOException {
    take(new AddressBook(), desktop -> desktop.click(new TextToolkit().point(18, 0)), "address-book-menu",
        "Find next");
  }

  @Test
  void theInventory() throws IOException {
    take(new Inventory(), desktop -> { }, "inventory", "Orders this week");
  }

  @Test
  void hello() throws IOException {
    take(new HelloWorld(), desktop -> { }, "hello", "Hello, world!");
  }

  @Test
  void aForm() throws IOException {
    take(new FormControls(), desktop -> { }, "form", "Ada Lovelace");
  }

  @Test
  void aListAndAnOpenDropDown() throws IOException {
    take(new ListsAndDropDowns(), desktop -> desktop.click(new TextToolkit().point(33, 6)), "lists", "Crate");
  }

  @Test
  void aMenuOpenUnderTheBar() throws IOException {
    take(new MenusAndStatusLine(), desktop -> desktop.click(new TextToolkit().point(5, 0)), "menus", "Open...");
  }

  @Test
  void aWindowThatScrolls() throws IOException {
    take(new ScrollingWindow(), desktop -> { }, "scrolling", "Two roads diverged");
  }

  /** Builds and draws the example, does this to its desktop, as a click finds what was drawn, and draws it again; the picture must show this text. */
  private static void take(Example example, Consumer<Desktop> pose, String name, String shown) throws IOException {
    Toolkit toolkit = new TextToolkit(java.util.Map.of("COLORTERM", "truecolor"));
    Desktop desktop = toolkit.desktop();
    example.build(toolkit, desktop);
    Screen screen = toolkit.screen(COLUMNS, ROWS);
    desktop.draw(screen);
    pose.accept(desktop);
    desktop.draw(screen);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    toolkit.terminal(out).show(screen);
    AnsiPicture picture = new AnsiPicture(COLUMNS, ROWS, out.toString(StandardCharsets.UTF_8));
    String into = System.getProperty("screenshots");
    if (into != null) {
      Path folder = Path.of(into);
      Files.createDirectories(folder);
      picture.png(folder.resolve(name + ".png"));
    }
    assertTrue(String.join("\n", screen.lines()).contains(shown), String.join("\n", screen.lines()));

  }
}
