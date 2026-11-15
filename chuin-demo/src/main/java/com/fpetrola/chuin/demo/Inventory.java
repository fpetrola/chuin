package com.fpetrola.chuin.demo;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.Window;
import java.util.List;

/**
 * A stock keeping application with several windows open at once, overlapping and casting shadows: the stock list,
 * the item picked from it, the week's orders, and a print dialog over them all.
 */
class Inventory implements Example {
  private static final List<String> STOCK = List.of("Floppy disks 3.5\"  120", "Floppy disks 5.25\"  40",
      "Dot matrix ribbon    35", "Continuous paper     80", "Serial mouse         12", "VGA monitor 14\"      6",
      "Keyboard, AT 101     18", "Modem 2400 baud       9", "Math coprocessor      4", "SIMM 1 MB            30",
      "Hard disk 40 MB       7", "Tape streamer         3");

  @Override
  public String name() {
    return "inventory";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Label said = toolkit.label("F6 moves between windows");
    desktop.show(menus(toolkit, said));
    desktop.show(status(toolkit, said));
    desktop.open(stock(toolkit));
    desktop.open(orders(toolkit));
    desktop.open(item(toolkit, said));
    desktop.open(print(toolkit, said));
  }

  private static Window stock(Toolkit toolkit) {
    Window stock = toolkit.window("Stock");
    stock.place(toolkit.area(1, 2, 31, 18));
    ListBox items = toolkit.listBox(STOCK, 13);
    items.next();
    items.next();
    items.next();
    stock.add(toolkit.label("~I~tem             Units", items), toolkit.point(1, 0));
    stock.add(items, toolkit.point(1, 1));
    return stock;
  }

  private static Window item(Toolkit toolkit, Label said) {
    Window item = toolkit.window("Continuous paper");
    item.place(toolkit.area(17, 11, 32, 11));
    InputLine units = toolkit.inputLine(6);
    "80".chars().forEach(letter -> units.type((char) letter));
    InputLine reorder = toolkit.inputLine(6);
    "25".chars().forEach(letter -> reorder.type((char) letter));
    item.add(toolkit.label("~U~nits:", units), toolkit.point(1, 1));
    item.add(units, toolkit.point(14, 1));
    item.add(toolkit.label("~R~eorder at:", reorder), toolkit.point(1, 2));
    item.add(reorder, toolkit.point(14, 2));
    CheckBox discontinued = toolkit.checkBox("~D~iscontinued");
    item.add(discontinued, toolkit.point(1, 4));
    item.add(said, toolkit.point(1, 6));
    item.add(toolkit.button("~O~rder"), toolkit.point(1, 8));
    return item;
  }

  private static Window orders(Toolkit toolkit) {
    Window orders = toolkit.window("Orders this week");
    orders.place(toolkit.area(36, 2, 42, 9));
    List<String> lines = List.of("#1041  Floppy disks 3.5\"  50", "#1042  Serial mouse        4",
        "#1043  Continuous paper   20", "#1044  SIMM 1 MB           8", "#1045  VGA monitor 14\"     2");
    for (int line = 0; line < lines.size(); line++) {
      orders.add(toolkit.label(lines.get(line)), toolkit.point(1, line + 1));
    }
    return orders;
  }

  private static Window print(Toolkit toolkit, Label said) {
    Window print = toolkit.window("Print");
    print.place(toolkit.area(47, 13, 32, 10));
    OptionGroup to = toolkit.optionGroup(List.of("Printer (LPT1)", "File", "Screen"));
    print.add(toolkit.label("~T~o:", to), toolkit.point(1, 0));
    print.add(to, toolkit.point(1, 1));
    CheckBox below = toolkit.checkBox("~B~elow reorder only");
    below.toggle();
    print.add(below, toolkit.point(1, 5));
    Button go = toolkit.button("~P~rint");
    go.onPress(pressed -> said.text("Printing the stock list"));
    print.add(go, toolkit.point(1, 7));
    print.add(toolkit.button("~C~ancel"), toolkit.point(13, 7));
    return print;
  }

  private static MenuBar menus(Toolkit toolkit, Label said) {
    MenuBar bar = toolkit.menuBar();
    Menu system = toolkit.menu("≡");
    system.add("~A~bout...", () -> said.text("Stock keeping built with cerco"));
    Menu stock = toolkit.menu("~S~tock");
    stock.add("~N~ew item", toolkit.functionKey(4), () -> said.text("A new item"));
    stock.add("~R~eceive...", () -> said.text("Goods received"));
    stock.add("~P~rint list...", toolkit.functionKey(9), () -> said.text("Printing the stock list"));
    Menu orders = toolkit.menu("~O~rders");
    orders.add("~N~ew order", () -> said.text("A new order"));
    orders.add("~W~eek's orders", () -> said.text("Orders this week"));
    Menu window = toolkit.menu("~W~indow");
    window.add("~N~ext", toolkit.functionKey(6), () -> { });
    bar.add(system);
    bar.add(stock);
    bar.add(orders);
    bar.add(window);
    return bar;
  }

  private static StatusLine status(Toolkit toolkit, Label said) {
    StatusLine status = toolkit.statusLine();
    status.add("~F1~ Help", toolkit.functionKey(1), () -> said.text("F6 moves between windows"));
    status.add("~F4~ New item", toolkit.functionKey(4), () -> said.text("A new item"));
    status.add("~F9~ Print", toolkit.functionKey(9), () -> said.text("Printing the stock list"));
    return status;
  }
}
