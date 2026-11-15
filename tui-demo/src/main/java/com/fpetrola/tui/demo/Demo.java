package com.fpetrola.tui.demo;

import com.fpetrola.tui.Button;
import com.fpetrola.tui.Desktop;
import com.fpetrola.tui.DropDown;
import com.fpetrola.tui.InputLine;
import com.fpetrola.tui.Label;
import com.fpetrola.tui.Menu;
import com.fpetrola.tui.MenuBar;
import com.fpetrola.tui.OptionGroup;
import com.fpetrola.tui.StatusLine;
import com.fpetrola.tui.TextToolkit;
import com.fpetrola.tui.Toolkit;
import com.fpetrola.tui.Window;
import java.util.List;

/**
 * An address card in the style of a Turbo Vision application: labelled fields, a group of options, a drop down, a
 * check box and buttons, under a menu bar and over a status line. Launch it through demo.sh; Ctrl+Q quits.
 */
public class Demo {
  private final Toolkit toolkit = new TextToolkit();
  private final Desktop desktop = toolkit.desktop();
  private final Label message = toolkit.label("Alt+letra va al campo; F10 abre el menú");
  private final InputLine name = toolkit.inputLine(28);
  private final InputLine city = toolkit.inputLine(20);
  private final OptionGroup country = toolkit.optionGroup(List.of("Argentina", "Uruguay", "Chile", "Otro ..."));
  private final DropDown state = toolkit.dropDown(List.of("Activo", "Inactivo", "Pendiente"));

  private Demo() {
  }

  public static void main(String[] args) {
    Demo demo = new Demo();
    demo.desktop.show(demo.menus());
    demo.desktop.show(demo.status());
    demo.desktop.open(demo.card());
    demo.toolkit.console(System.in, System.out, demo.toolkit.screen(80, 24)).run(demo.desktop);
  }

  /** The card: labelled fields on the left, the country group on the right, buttons along the bottom. */
  private Window card() {
    Window card = toolkit.window("Agenda (ficha 1/1)");
    card.place(toolkit.area(2, 2, 62, 18));
    card.add(toolkit.label("~N~ombre:", name), toolkit.point(1, 1));
    card.add(name, toolkit.point(11, 1));
    card.add(toolkit.label("~C~iudad:", city), toolkit.point(1, 3));
    card.add(city, toolkit.point(11, 3));
    card.add(toolkit.label("~E~stado:", state), toolkit.point(1, 5));
    card.add(state, toolkit.point(11, 5));
    card.add(toolkit.checkBox("~R~ecordar"), toolkit.point(11, 7));
    card.add(toolkit.label("~P~aís:", country), toolkit.point(44, 1));
    card.add(country, toolkit.point(44, 2));
    card.add(message, toolkit.point(1, 10));
    addButtons(card);
    return card;
  }

  private void addButtons(Window card) {
    Button save = toolkit.button("~G~uardar");
    Button clear = toolkit.button("~B~orrar");
    save.onPress(pressed -> save());
    clear.onPress(pressed -> message.text("Ficha borrada"));
    card.add(save, toolkit.point(11, 13));
    card.add(clear, toolkit.point(26, 13));
  }

  private void save() {
    message.text("Guardado: " + name.text() + ", " + city.text() + ", " + state.chosen().orElse(""));
  }

  /** ≡ and Archivo, whose items carry the keys that run them from anywhere. */
  private MenuBar menus() {
    MenuBar bar = toolkit.menuBar();
    Menu system = toolkit.menu("≡");
    system.add("~A~cerca de", () -> message.text("Una TUI hecha con cerco"));
    Menu file = toolkit.menu("~A~rchivo");
    file.add("~G~uardar", toolkit.functionKey(2), this::save);
    file.add("~L~impiar", toolkit.functionKey(3), () -> message.text(""));
    file.separator();
    file.add("~A~yuda", toolkit.functionKey(1), () -> desktop.open(help()));
    bar.add(system);
    bar.add(file);
    return bar;
  }

  /** The keys bound on the status line; each one does what it says. */
  private StatusLine status() {
    StatusLine status = toolkit.statusLine();
    status.add("~F1~ Ayuda", toolkit.functionKey(1), () -> desktop.open(help()));
    status.add("~F2~ Guardar", toolkit.functionKey(2), this::save);
    status.add("~F3~ Limpiar", toolkit.functionKey(3), () -> message.text(""));
    return status;
  }

  /** A window that lists what each key does; Ctrl+W closes it. */
  private Window help() {
    Window help = toolkit.window("Ayuda");
    help.place(toolkit.area(20, 6, 40, 11));
    List<String> lines = List.of(
        "Tab        pasa al siguiente",
        "Alt+letra  va a lo resaltado",
        "Enter      aprieta o elige",
        "Espacio    tilda",
        "Flechas    lista, opciones, ventana",
        "F6         cambia de ventana",
        "F10        abre el menú; Esc lo cierra",
        "Ctrl+W     cierra la ventana; Ctrl+Q sale");
    for (int line = 0; line < lines.size(); line++) {
      help.add(toolkit.label(lines.get(line)), toolkit.point(1, line));
    }
    return help;
  }
}
