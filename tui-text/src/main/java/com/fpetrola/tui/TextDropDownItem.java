package com.fpetrola.tui;

/** An item of a drop down's open list, with the drop down that shows it. */
record TextDropDownItem(DropDown dropDown, String text) implements DropDownItem {
}
