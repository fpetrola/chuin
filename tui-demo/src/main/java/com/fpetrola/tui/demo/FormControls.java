package com.fpetrola.tui.demo;

import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.view.Button;
import com.fpetrola.tui.view.CheckBox;
import com.fpetrola.tui.view.InputLine;
import com.fpetrola.tui.view.Label;
import com.fpetrola.tui.view.OptionGroup;
import com.fpetrola.tui.view.Window;
import java.util.List;

/** A dialog with the usual form controls: labelled input lines, check boxes, a group of options and buttons. */
class FormControls implements Example {
  @Override
  public String name() {
    return "form";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Window dialog = toolkit.window("New contact");
    dialog.place(toolkit.area(8, 3, 62, 17));
    InputLine name = toolkit.inputLine(30);
    InputLine email = toolkit.inputLine(30);
    "Ada Lovelace".chars().forEach(letter -> name.type((char) letter));
    "ada@example.org".chars().forEach(letter -> email.type((char) letter));
    dialog.add(toolkit.label("~N~ame:", name), toolkit.point(2, 1));
    dialog.add(name, toolkit.point(10, 1));
    dialog.add(toolkit.label("~E~mail:", email), toolkit.point(2, 3));
    dialog.add(email, toolkit.point(10, 3));
    CheckBox subscribe = toolkit.checkBox("~S~ubscribe to the newsletter");
    subscribe.toggle();
    dialog.add(subscribe, toolkit.point(10, 5));
    dialog.add(toolkit.checkBox("~V~IP customer"), toolkit.point(10, 6));
    OptionGroup contact = toolkit.optionGroup(List.of("E-mail", "Phone", "Post"));
    contact.next();
    dialog.add(toolkit.label("~C~ontact by:", contact), toolkit.point(44, 1));
    dialog.add(contact, toolkit.point(44, 2));
    Label said = toolkit.label("Fill in the card and press OK");
    dialog.add(said, toolkit.point(2, 9));
    Button ok = toolkit.button("~O~K");
    Button cancel = toolkit.button("~C~ancel");
    ok.onPress(pressed -> said.text("Saved " + name.text()));
    cancel.onPress(pressed -> said.text("Nothing saved"));
    dialog.add(ok, toolkit.point(10, 12));
    dialog.add(cancel, toolkit.point(22, 12));
    desktop.open(dialog);
  }
}
