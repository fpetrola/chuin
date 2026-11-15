package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.screen.Terminal;
import com.fpetrola.chuin.view.Button;
import com.fpetrola.chuin.view.CheckBox;
import com.fpetrola.chuin.view.DropDown;
import com.fpetrola.chuin.view.InputLine;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ListBox;
import com.fpetrola.chuin.view.OptionGroup;
import com.fpetrola.chuin.view.ScrollPanel;
import com.fpetrola.chuin.view.View;
import com.fpetrola.chuin.view.Window;

import java.util.List;

import java.io.InputStream;
import java.io.OutputStream;

public interface Toolkit {
  Desktop desktop();

  Window window(String title);

  /** A panel this many columns and rows that shows what it holds from its top left, with its bars in them. */
  ScrollPanel scrollPanel(View content, int columns, int rows);

  Button button(String caption);

  Label label(String text);

  /** A label for a view: its highlighted letter, with Alt, reaches that view. */
  Label label(String text, View linked);

  CheckBox checkBox(String text);

  InputLine inputLine(int width);

  /** A list of items that shows this many rows at a time, with a scroll bar beside them. */
  ListBox listBox(List<String> items, int rows);

  /** A drop down with these items, the first one chosen. */
  DropDown dropDown(List<String> items);

  /** A group of options, one per row, with the first one chosen. */
  OptionGroup optionGroup(List<String> options);

  /** An empty menu bar; menus are added to it left to right. */
  MenuBar menuBar();

  /** An empty status line; entries are added to it left to right. */
  StatusLine statusLine();

  Menu menu(String title);

  Key enterKey();

  Key tabKey();

  Key spaceKey();

  /** The function key F1 to F12 by its number, named as the one the keyboard reads, so it can be bound. */
  Key functionKey(int number);

  /** The escape key alone. */
  Key escapeKey();

  Screen screen(int columns, int rows);

  Area area(int column, int row, int columns, int rows);

  Point point(int column, int row);

  Terminal terminal(OutputStream out);

  Keyboard keyboard(InputStream in);

  Console console(InputStream in, OutputStream out, Screen screen);
}
