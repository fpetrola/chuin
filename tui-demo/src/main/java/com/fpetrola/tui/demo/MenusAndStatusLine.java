package com.fpetrola.tui.demo;

import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.menu.Menu;
import com.fpetrola.tui.menu.MenuBar;
import com.fpetrola.tui.menu.StatusLine;
import com.fpetrola.tui.view.Label;
import com.fpetrola.tui.view.Window;

/** A menu bar whose items carry the keys that run them, a status line of keys, and a window they act on. */
class MenusAndStatusLine implements Example {
  @Override
  public String name() {
    return "menus";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Window notes = toolkit.window("Notes");
    notes.place(toolkit.area(14, 5, 52, 12));
    Label said = toolkit.label("F10 opens the menu, Alt+F the File menu");
    notes.add(said, toolkit.point(2, 2));
    MenuBar bar = toolkit.menuBar();
    Menu system = toolkit.menu("≡");
    system.add("~A~bout", () -> said.text("A text UI built with cerco"));
    Menu file = toolkit.menu("~F~ile");
    file.add("~N~ew", toolkit.functionKey(4), () -> said.text("New note"));
    file.add("~O~pen...", toolkit.functionKey(3), () -> said.text("Open a note"));
    file.add("~S~ave", toolkit.functionKey(2), () -> said.text("Note saved"));
    file.separator();
    file.add("E~x~it", () -> said.text("Ctrl+Q quits"));
    Menu edit = toolkit.menu("~E~dit");
    edit.add("~C~ut", () -> said.text("Cut"));
    edit.add("C~o~py", () -> said.text("Copied"));
    edit.add("~P~aste", () -> said.text("Pasted"));
    Menu help = toolkit.menu("~H~elp");
    help.add("~K~eys", toolkit.functionKey(1), () -> said.text("Tab, Alt+letter, F10, Esc"));
    bar.add(system);
    bar.add(file);
    bar.add(edit);
    bar.add(help);
    StatusLine status = toolkit.statusLine();
    status.add("~F1~ Help", toolkit.functionKey(1), () -> said.text("Tab, Alt+letter, F10, Esc"));
    status.add("~F2~ Save", toolkit.functionKey(2), () -> said.text("Note saved"));
    status.add("~F3~ Open", toolkit.functionKey(3), () -> said.text("Open a note"));
    desktop.show(bar);
    desktop.show(status);
    desktop.open(notes);
  }
}
