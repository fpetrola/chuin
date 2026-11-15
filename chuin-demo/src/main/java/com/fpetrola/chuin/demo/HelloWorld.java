package com.fpetrola.chuin.demo;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.Window;

/** The smallest application: a window with a label and a button that changes it. */
class HelloWorld implements Example {
  @Override
  public String name() {
    return "hello";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Window window = toolkit.window("Hello");
    window.place(toolkit.area(20, 7, 40, 10));
    Label greeting = toolkit.label("Hello, world!");
    Button greet = toolkit.button("~G~reet");
    greet.onPress(pressed -> greeting.text("Hello again, world!"));
    window.add(greeting, toolkit.point(2, 1));
    window.add(greet, toolkit.point(2, 4));
    desktop.open(window);
  }
}
