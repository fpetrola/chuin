package com.fpetrola.chuin.demo;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.Window;
import java.util.List;

/**
 * An address database in the style of the DOS applications built with Turbo Vision: a record card of labelled fields,
 * a group of options, check boxes, a drop down and buttons, beside a list of the records, under a menu bar and over a
 * status line of keys.
 */
class AddressBook implements Example {
  private static final List<String> PEOPLE = List.of("Alvarez, Lucia", "Brandt, Oskar", "Castro, Elena",
      "Dubois, Marc", "Eklund, Sara", "Fischer, Jonas", "Garcia, Ines", "Hayes, Nora", "Ito, Kenji", "Jansen, Pieter",
      "Kowalski, Anna", "Lund, Erik", "Moreau, Claire");

  @Override
  public String name() {
    return "address-book";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Label said = toolkit.label("Alt+letter jumps to a field; F10 opens the menu");
    desktop.show(menus(toolkit, said));
    desktop.show(status(toolkit, said));
    desktop.open(records(toolkit));
    desktop.open(card(toolkit, said));
  }

  /** The card of the record shown: who, where, how to reach them, and what to send them. */
  private Window card(Toolkit toolkit, Label said) {
    Window card = toolkit.window("Address database  (record 7 of 13)");
    card.place(toolkit.area(0, 1, 58, 22));
    field(toolkit, card, "~F~irst name:", "Ines", 1);
    field(toolkit, card, "~L~ast name:", "Garcia", 2);
    field(toolkit, card, "C~o~mpany:", "Example Software Ltd.", 3);
    field(toolkit, card, "S~t~reet:", "12 Sample Lane", 5);
    field(toolkit, card, "~Z~IP, city:", "12345 Springfield", 6);
    field(toolkit, card, "~P~hone:", "+1 555 0142", 8);
    field(toolkit, card, "~E~-mail:", "ines.garcia@example.com", 9);
    DropDown group = toolkit.dropDown(List.of("Colleague", "Customer", "Family", "Friend", "Supplier"));
    card.add(toolkit.label("~G~roup:", group), toolkit.point(1, 11));
    card.add(group, toolkit.point(14, 11));
    OptionGroup title = toolkit.optionGroup(List.of("Mr.", "Ms.", "Mx.", "Dr.", "Prof."));
    title.next();
    card.add(toolkit.label("T~i~tle:", title), toolkit.point(44, 1));
    card.add(title, toolkit.point(44, 2));
    CheckBox holiday = toolkit.checkBox("~H~oliday card");
    holiday.toggle();
    card.add(holiday, toolkit.point(14, 13));
    card.add(toolkit.checkBox("~N~ewsletter"), toolkit.point(14, 14));
    card.add(said, toolkit.point(1, 16));
    buttons(toolkit, card, said);
    return card;
  }

  /** A label and an input line on a row of the card, the line filled in. */
  private static void field(Toolkit toolkit, Window card, String caption, String text, int row) {
    InputLine line = toolkit.inputLine(26);
    text.chars().forEach(letter -> line.type((char) letter));
    card.add(toolkit.label(caption, line), toolkit.point(1, row));
    card.add(line, toolkit.point(14, row));
  }

  private static void buttons(Toolkit toolkit, Window card, Label said) {
    Button previous = toolkit.button("<< P~r~ev");
    Button next = toolkit.button("Ne~x~t >>");
    Button save = toolkit.button("~S~ave");
    previous.onPress(pressed -> said.text("Record 6 of 13"));
    next.onPress(pressed -> said.text("Record 8 of 13"));
    save.onPress(pressed -> said.text("Record saved"));
    card.add(previous, toolkit.point(1, 18));
    card.add(next, toolkit.point(15, 18));
    card.add(save, toolkit.point(29, 18));
  }

  /** Every record by name, the one on the card selected. */
  private static Window records(Toolkit toolkit) {
    Window records = toolkit.window("Records");
    records.place(toolkit.area(58, 1, 22, 22));
    ListBox names = toolkit.listBox(PEOPLE, 18);
    for (int step = 0; step < 6; step++) {
      names.next();
    }
    records.add(names, toolkit.point(1, 1));
    return records;
  }

  private static MenuBar menus(Toolkit toolkit, Label said) {
    MenuBar bar = toolkit.menuBar();
    Menu system = toolkit.menu("≡");
    system.add("~A~bout...", () -> said.text("An address database built with cerco"));
    Menu file = toolkit.menu("~F~ile");
    file.add("~N~ew record", toolkit.functionKey(4), () -> said.text("A new, empty record"));
    file.add("~O~pen database...", toolkit.functionKey(3), () -> said.text("Open a database"));
    file.add("~S~ave", toolkit.functionKey(2), () -> said.text("Record saved"));
    file.separator();
    file.add("~P~rint labels...", () -> said.text("Printing labels"));
    file.add("E~x~it", () -> said.text("Ctrl+Q quits"));
    Menu edit = toolkit.menu("~E~dit");
    edit.add("~U~ndo", () -> said.text("Undone"));
    edit.separator();
    edit.add("Cu~t~", () -> said.text("Cut"));
    edit.add("~C~opy", () -> said.text("Copied"));
    edit.add("~P~aste", () -> said.text("Pasted"));
    Menu search = toolkit.menu("~S~earch");
    search.add("~F~ind...", toolkit.functionKey(7), () -> said.text("Find a record"));
    search.add("Find ~n~ext", toolkit.functionKey(8), () -> said.text("Next match"));
    search.add("~G~o to record...", () -> said.text("Go to a record"));
    Menu options = toolkit.menu("~O~ptions");
    options.add("~C~olors...", () -> said.text("The DOS palette"));
    options.add("~S~ort by...", () -> said.text("Sorted by last name"));
    bar.add(system);
    bar.add(file);
    bar.add(edit);
    bar.add(search);
    bar.add(options);
    return bar;
  }

  private static StatusLine status(Toolkit toolkit, Label said) {
    StatusLine status = toolkit.statusLine();
    status.add("~F1~ Help", toolkit.functionKey(1), () -> said.text("Tab moves, Alt+letter jumps, F10 menu"));
    status.add("~F2~ Save", toolkit.functionKey(2), () -> said.text("Record saved"));
    status.add("~F4~ New", toolkit.functionKey(4), () -> said.text("A new, empty record"));
    status.add("~F7~ Find", toolkit.functionKey(7), () -> said.text("Find a record"));
    return status;
  }
}
