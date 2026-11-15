package com.fpetrola.chuin.view;

/** An item an open drop down shows in its list. */
public interface DropDownItem {
  /** The drop down that shows it. */
  DropDown dropDown();

  String text();

  /** Makes this item the chosen one of its drop down and closes the list. */
  void choose();
}
