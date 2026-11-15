package com.fpetrola.chuin.demo;

import com.fpetrola.chuin.Desktop;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.Window;
import java.util.List;

/** A list box longer than it shows, with its scroll bar, and a drop down whose list opens under it. */
class ListsAndDropDowns implements Example {
  @Override
  public String name() {
    return "lists";
  }

  @Override
  public void build(Toolkit toolkit, Desktop desktop) {
    Window window = toolkit.window("Fruit stand");
    window.place(toolkit.area(10, 3, 60, 17));
    ListBox fruit = toolkit.listBox(List.of("Apple", "Apricot", "Banana", "Blackberry", "Cherry", "Coconut", "Fig",
        "Grape", "Kiwi", "Lemon", "Lime", "Mango", "Melon", "Orange", "Papaya", "Peach", "Pear", "Plum"), 9);
    fruit.next();
    fruit.next();
    DropDown size = toolkit.dropDown(List.of("Small box", "Medium box", "Large box", "Crate"));
    Label order = toolkit.label("");
    Button add = toolkit.button("~A~dd to order");
    add.onPress(pressed -> order.text(size.chosen().orElse("") + " of " + fruit.selected().orElse("")));
    window.add(toolkit.label("~F~ruit:", fruit), toolkit.point(2, 1));
    window.add(fruit, toolkit.point(2, 2));
    window.add(toolkit.label("~P~acking:", size), toolkit.point(22, 1));
    window.add(size, toolkit.point(22, 2));
    window.add(add, toolkit.point(36, 2));
    window.add(order, toolkit.point(22, 10));
    desktop.open(window);
  }
}
