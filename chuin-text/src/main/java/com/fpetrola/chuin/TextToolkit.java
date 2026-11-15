package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.input.KeyVisitor;
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
import java.util.Map;
import java.util.Optional;

import java.io.InputStream;
import java.io.OutputStream;

public class TextToolkit implements Toolkit {
  private final Desktop desktop = new MenuDesktop(new PointerWindows(new TextDesktop()));

  /** Exact RGB when the terminal says it has 24-bit colour, as COLORTERM does with truecolor or 24bit; named otherwise. */
  private final Palette palette;

  public TextToolkit() {
    this(System.getenv());
  }

  /** A toolkit for a terminal described by these environment variables. */
  public TextToolkit(Map<String, String> environment) {
    String colorterm = environment.getOrDefault("COLORTERM", "");
    this.palette = colorterm.equals("truecolor") || colorterm.equals("24bit") ? DosPalette.RGB : DosPalette.NAMED;
  }

  @Override
  public Desktop desktop() {
    return desktop;
  }

  @Override
  public Window window(String title) {
    return new TextWindow(title);
  }

  @Override
  public ScrollPanel scrollPanel(View content, int columns, int rows) {
    return new TextScrollPanel(content, () -> new TextSize(columns, rows));
  }

  @Override
  public Button button(String caption) {
    return new TextButton(caption);
  }

  @Override
  public Label label(String text, View linked) {
    return new TextLabel(text, Optional.of(linked));
  }

  @Override
  public Label label(String text) {
    return new TextLabel(text, Optional.empty());
  }

  @Override
  public CheckBox checkBox(String text) {
    return new TextCheckBox(text);
  }

  @Override
  public OptionGroup optionGroup(List<String> options) {
    return new TextOptionGroup(options);
  }

  @Override
  public DropDown dropDown(List<String> items) {
    return new TextDropDown(items);
  }

  @Override
  public ListBox listBox(List<String> items, int rows) {
    return new TextListBox(items, rows);
  }

  @Override
  public InputLine inputLine(int width) {
    return new TextInputLine(width);
  }

  @Override
  public Key enterKey() {
    return KeyVisitor::enter;
  }

  @Override
  public Key tabKey() {
    return KeyVisitor::tab;
  }

  @Override
  public Key spaceKey() {
    return KeyVisitor::space;
  }

  @Override
  public Key functionKey(int number) {
    return visitor -> visitor.function(number);
  }

  @Override
  public Key escapeKey() {
    return KeyVisitor::escape;
  }

  @Override
  public Screen screen(int columns, int rows) {
    return new TextScreen(columns, rows);
  }

  @Override
  public Area area(int column, int row, int columns, int rows) {
    return new TextArea(column, row, columns, rows);
  }

  @Override
  public Point point(int column, int row) {
    return new TextPoint(column, row);
  }

  @Override
  public Terminal terminal(OutputStream out) {
    return new AnsiTerminal(out, palette);
  }

  @Override
  public StatusLine statusLine() {
    return new TextStatusLine();
  }

  @Override
  public MenuBar menuBar() {
    return new TextMenuBar();
  }

  @Override
  public Menu menu(String title) {
    return new TextMenu(title);
  }

  @Override
  public Keyboard keyboard(InputStream in) {
    return new StreamKeyboard(in, this);
  }

  @Override
  public Console console(InputStream in, OutputStream out, Screen screen) {
    return new TextConsole(keyboard(in), terminal(out), out, screen);
  }
}
