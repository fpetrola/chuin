package com.fpetrola.tui.demo;

import com.fpetrola.tui.Button;
import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.TextToolkit;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.Window;

/** Runs a window with a button on this process's console. Launch it through demo.sh. */
public class Demo {
  private Demo() {
  }

  public static void main(String[] args) {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    Window window = toolkit.window("Demo");
    Button ok = toolkit.button("OK");

    window.place(toolkit.area(2, 1, 30, 7));
    window.add(ok, toolkit.point(11, 2));
    desktop.open(window);
    toolkit.console(System.in, System.out, toolkit.screen(80, 24)).run(desktop);
  }
}
