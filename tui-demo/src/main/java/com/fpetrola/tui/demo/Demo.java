package com.fpetrola.tui.demo;

import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.TextToolkit;
import com.fpetrola.tui.Toolkit;
import java.util.List;

/**
 * Runs one of the examples on this terminal, across the whole of it: the one named first, or the address book when
 * none is. Launch it through demo.sh, which tells it the terminal's size; Ctrl+Q quits.
 */
public class Demo {
  /** Every example, the first one run when none is named. */
  private static final List<Example> SHOWN = List.of(new AddressBook(), new Inventory(), new HelloWorld(),
      new FormControls(), new ListsAndDropDowns(), new MenusAndStatusLine(), new ScrollingWindow());

  private Demo() {
  }

  /**
   * Takes an example's name, then the terminal's rows and columns as stty size gives them, each optional: the
   * address book, and 24 by 80, when not told. An unknown name lists the names there are.
   */
  public static void main(String[] args) {
    List<String> given = List.of(args);
    List<String> named = given.size() % 2 == 1 ? given.subList(0, 1) : List.of(SHOWN.getFirst().name());
    List<String> size = given.size() >= 2 ? given.subList(given.size() - 2, given.size()) : List.of("24", "80");
    SHOWN.stream().filter(example -> example.name().equals(named.getFirst())).findFirst().ifPresentOrElse(
        example -> run(example, Integer.parseInt(size.get(0)), Integer.parseInt(size.get(1))),
        () -> System.out.println("Examples: " + SHOWN.stream().map(Example::name).toList()));
  }

  private static void run(Example example, int rows, int columns) {
    Toolkit toolkit = new TextToolkit();
    Desktop desktop = toolkit.desktop();
    example.build(toolkit, desktop);
    toolkit.console(System.in, System.out, toolkit.screen(columns, rows)).run(desktop);
  }
}
