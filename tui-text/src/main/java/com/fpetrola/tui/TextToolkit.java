package com.fpetrola.tui;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;
import java.io.OutputStream;

public class TextToolkit implements Toolkit {
  private final TextKeys keys = new TextKeys();
  private final Desktop desktop = new MenuDesktop(new TextDesktop());

  public TextToolkit() {
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
  public RadioButtons radioButtons(List<String> options) {
    return new TextRadioButtons(options);
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
  public Keys keys() {
    return keys;
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
    return new AnsiTerminal(out);
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
    return new StreamKeyboard(in, keys);
  }

  @Override
  public Console console(InputStream in, OutputStream out, Screen screen) {
    return new TextConsole(keyboard(in), terminal(out), out, screen);
  }
}
