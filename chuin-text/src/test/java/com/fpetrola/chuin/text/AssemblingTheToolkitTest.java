package com.fpetrola.chuin.text;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.Keyboard;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.text.desktop.TextConsole;
import com.fpetrola.chuin.text.desktop.TextDesktop;
import com.fpetrola.chuin.text.input.StreamKeyboard;
import com.fpetrola.chuin.text.menu.MenuDesktop;
import com.fpetrola.chuin.text.menu.TextMenu;
import com.fpetrola.chuin.text.menu.TextMenuBar;
import com.fpetrola.chuin.text.menu.TextStatusLine;
import com.fpetrola.chuin.text.screen.AnsiTerminal;
import com.fpetrola.chuin.text.screen.DosPalette;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** The parts TextToolkit puts together, each made from outside its package, as the toolkit makes them. */
class AssemblingTheToolkitTest {
  private final Toolkit toolkit = new TextToolkit();

  @Test
  void aDesktopWithMenusShowsItsBarAndStatusLine() {
    Desktop desktop = new MenuDesktop(new TextDesktop());
    MenuBar bar = new TextMenuBar();
    Menu file = new TextMenu("~F~ile");
    StatusLine status = new TextStatusLine();
    file.add("~N~ew", () -> { });
    bar.add(file);
    status.add("~F1~ Help", toolkit.functionKey(1), () -> { });
    desktop.show(bar);
    desktop.show(status);
    Screen screen = toolkit.screen(30, 4);

    desktop.draw(screen);

    assertEquals("File", file.title().replace("~", ""));
    assertTrue(screen.lines().get(0).contains("File"));
    assertTrue(screen.lines().get(3).contains("F1 Help"));
  }

  @Test
  void aConsoleShowsTheDesktopAndReadsKeysFromAKeyboardUntilItsInputEnds() {
    Keyboard keyboard = new StreamKeyboard(new ByteArrayInputStream("x".getBytes(StandardCharsets.UTF_8)), toolkit);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Screen screen = toolkit.screen(10, 2);
    Desktop desktop = new MenuDesktop(new TextDesktop());

    new TextConsole(keyboard, new AnsiTerminal(out, DosPalette.NAMED), out, screen).run(desktop);

    assertTrue(out.size() > 0);
  }
}
