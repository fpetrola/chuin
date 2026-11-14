package com.fpetrola.tui;

/** An item an open drop down shows in its list. */
public interface DropDownItem {
  /** The drop down that shows it. */
  DropDown dropDown();

  String text();
}
