package com.fpetrola.chuin;

import com.fpetrola.chuin.geometry.Point;
import com.fpetrola.chuin.input.Key;
import com.fpetrola.chuin.input.KeyVisitor;
import com.fpetrola.chuin.menu.Menu;
import com.fpetrola.chuin.menu.MenuBar;
import com.fpetrola.chuin.menu.MenuItem;
import com.fpetrola.chuin.menu.MenuVisitor;
import com.fpetrola.chuin.menu.StatusItem;
import com.fpetrola.chuin.menu.StatusLine;
import com.fpetrola.chuin.menu.StatusVisitor;
import com.fpetrola.chuin.screen.Screen;
import com.fpetrola.chuin.view.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A desktop with a menu bar over the windows of another. While no menu is open, keys and clicks go to the windows,
 * except F10 and clicks on the bar. While a menu is open, keys and clicks stay with the menus: the arrows walk items
 * and menus, Enter or a click on an item runs it, and F10 or a click away closes it. Each of the two is a state.
 */
class MenuDesktop implements Desktop {
  private static final int MENU_KEY = 10;
  /** The name Escape goes by, which an open menu keeps for itself whatever is bound to it. */
  private static final String ESCAPE = "escape";
  private final Windows windows;
  private MenuBar bar = new TextMenuBar();
  private Desktop state = new Closed();
  /** The status line across the bottom row, once one is shown. */
  private Optional<StatusLine> status = Optional.empty();
  /** How many rows the screen had when last drawn, so a click can tell the bottom row. */
  private int rows;
  /** The command of each bound key, by the key's name. */
  private final Map<String, Runnable> bindings = new HashMap<>();

  MenuDesktop(Windows windows) {
    this.windows = windows;
  }

  @Override
  public void open(Window window) {
    windows.open(window);
  }

  /** Shows the line across the bottom row and binds the key of each entry to it. */
  @Override
  public void show(StatusLine shown) {
    status = Optional.of(shown);
    shown.accept(new StatusVisitor() {
      @Override
      public void visitStatusItem(StatusItem item) {
        bind(item.key(), item::run);
      }
    });
  }

  /** Shows the bar across the top row and binds the key of each of its items to that item. */
  @Override
  public void show(MenuBar shown) {
    bar = shown;
    shown.accept(new MenuVisitor() {
      @Override
      public void visitMenuItem(MenuItem item) {
        item.key().ifPresent(key -> bind(key, item::run));
      }
    });
  }

  @Override
  public void bind(Key key, Runnable command) {
    bindings.put(name(key), command);
  }

  /** Each state decides whether a bound key runs: with a menu open, Escape closes the menu first. */
  @Override
  public void type(Key key) {
    state.type(key);
  }

  /** Runs the command bound to this key, closing any open menu, and tells whether there was one. */
  private boolean ranBound(Key key) {
    Runnable bound = bindings.get(name(key));
    if (bound != null) {
      state = new Closed();
      bound.run();
    }
    return bound != null;
  }

  private static String name(Key key) {
    StringBuilder name = new StringBuilder();
    key.accept(new KeyName(name));
    return name.toString();
  }

  /** A click on the status row runs the entry under it, or nothing, and closes any open menu; others go to the state. */
  @Override
  /** A drag goes to the windows, which take it only if the click that started it went to them. */
  public void drag(Point cell) {
    windows.drag(cell);
  }

  @Override
  public void release(Point cell) {
    windows.release(cell);
  }

  @Override
  public void click(Point cell) {
    boolean onStatus = status.isPresent() && cell.row() == rows - 1;
    if (onStatus) {
      state = new Closed();
      status.get().accept(new StatusClick(cell.column()));
    } else {
      state.click(cell);
    }
  }

  @Override
  public void draw(Screen screen) {
    rows = screen.lines().size();
    windows.draw(screen);
    status.ifPresent(line -> line.accept(new StatusPainter(screen)));
    state.draw(screen);
  }

  /** The titles of the bar's menus where the bar puts them, left to right, each starting where the one before ends. */
  private List<PlacedTitle> placed() {
    List<PlacedTitle> placed = new ArrayList<>();
    bar.accept(new MenuVisitor() {
      private final EntryPlacer placer = new TextEntryPlacer();

      @Override
      public void visitMenu(Menu menu) {
        placed.add(new TextPlacedTitle(menu, placer.place(new TextCaption(menu.title()))));
      }
    });
    return placed;
  }

  private Optional<PlacedTitle> titleAt(Point cell) {
    return placed().stream().filter(title -> title.covers(cell)).findFirst();
  }

  private Optional<PlacedTitle> answering(char letter) {
    return placed().stream().filter(title -> title.answers(letter)).findFirst();
  }

  /** The title a step to the right of this one, around from the last to the first; left is a step back. */
  private PlacedTitle beside(PlacedTitle title, int step) {
    List<PlacedTitle> placed = placed();
    int at = placed.stream().map(PlacedTitle::menu).toList().indexOf(title.menu());
    return placed.get(Math.floorMod(at + step, placed.size()));
  }

  /** The state every desktop call lands in; open and show are the menu desktop's, whatever the state. */
  private abstract class MenuState implements Desktop {
    @Override
    public void drag(Point cell) {
      MenuDesktop.this.drag(cell);
    }

    @Override
    public void release(Point cell) {
      MenuDesktop.this.release(cell);
    }

    @Override
    public void bind(Key key, Runnable command) {
      MenuDesktop.this.bind(key, command);
    }

    @Override
    public void show(StatusLine shown) {
      MenuDesktop.this.show(shown);
    }

    @Override
    public void open(Window window) {
      MenuDesktop.this.open(window);
    }

    @Override
    public void show(MenuBar shown) {
      MenuDesktop.this.show(shown);
    }
  }

  /** No menu is open: everything goes to the windows, but F10 and clicks on the bar, which open a menu. */
  private class Closed extends MenuState {
    @Override
    public void type(Key key) {
      if (name(key).equals(ESCAPE)) {
        windows.escape(() -> typeAsUsual(key));
      } else {
        typeAsUsual(key);
      }
    }

    private void typeAsUsual(Key key) {
      if (!ranBound(key)) {
        key.accept(new ClosedKeys(key));
      }
    }

    @Override
    public void click(Point cell) {
      boolean onBar = cell.row() == 0 && !placed().isEmpty();
      titleAt(cell).ifPresentOrElse(title -> state = new Opened(title), () -> {
        if (!onBar) {
          windows.click(cell);
        }
      });
    }

    @Override
    public void draw(Screen screen) {
      if (!placed().isEmpty()) {
        bar.accept(new MenuPainter(screen, Optional.empty()));
      }
    }
  }

  /** A menu is open, with one of its items selected: keys and clicks stay with the menus. */
  private class Opened extends MenuState {
    private final PlacedTitle open;
    /** Which of its items is selected. */
    private final Cursor item;

    Opened(PlacedTitle open) {
      this.open = open;
      this.item = new TextCursor(open.items().size());
    }

    @Override
    public void type(Key key) {
      if (name(key).equals(ESCAPE)) {
        state = new Closed();
      } else if (!ranBound(key)) {
        key.accept(new OpenedKeys(this));
      }
    }

    @Override
    public void click(Point cell) {
      Optional<PlacedTitle> title = titleAt(cell);
      Optional<MenuItem> clicked = open.itemAt(cell);
      state = title.<Desktop>map(Opened::new).orElseGet(Closed::new);
      if (title.isEmpty()) {
        clicked.ifPresent(MenuItem::run);
      }
    }

    /** The items of the open menu, top to bottom. */
    private List<MenuItem> items() {
      return open.items();
    }

    @Override
    public void draw(Screen screen) {
      screen.hideCursor();
      bar.accept(new MenuPainter(screen, Optional.of(open.menu())));
      open.drawOpen(screen, item.at());
    }
  }

  /** Keys while no menu is open: F10 opens the first menu when there is a bar, Alt with a letter the menu that answers; any other key goes to the windows. */
  private class ClosedKeys extends ToWindows {
    ClosedKeys(Key key) {
      super(key, windows);
    }

    @Override
    public void function(int number) {
      if (number == MENU_KEY && !placed().isEmpty()) {
        state = new Opened(placed().getFirst());
      } else {
        super.function(number);
      }
    }

    /** A menu whose title answers opens; otherwise the windows get it. */
    @Override
    public void alt(char letter) {
      answering(letter).ifPresentOrElse(menu -> state = new Opened(menu), () -> super.alt(letter));
    }
  }

  /** Keys while a menu is open: they stay with the menus. */
  private class OpenedKeys implements KeyVisitor {
    private final Opened opened;

    OpenedKeys(Opened opened) {
      this.opened = opened;
    }

    @Override
    public void enter() {
      List<MenuItem> items = opened.items();
      state = new Closed();
      if (opened.item.at() < items.size()) {
        items.get(opened.item.at()).run();
      }
    }

    @Override
    public void up() {
      opened.item.step(-1);
    }

    @Override
    public void down() {
      opened.item.step(1);
    }

    @Override
    public void left() {
      state = new Opened(beside(opened.open, -1));
    }

    @Override
    public void right() {
      state = new Opened(beside(opened.open, 1));
    }

    @Override
    public void function(int number) {
      if (number == MENU_KEY) {
        state = new Closed();
      }
    }

    @Override
    public void escape() {
      state = new Closed();
    }

    /** The item of the open menu whose caption answers runs, and the menu closes. */
    @Override
    public void character(char typed) {
      for (MenuItem each : opened.items()) {
        if (new TextCaption(each.text()).answers(typed)) {
          state = new Closed();
          each.run();
          return;
        }
      }
    }

    /** Another menu whose title answers opens instead. */
    @Override
    public void alt(char letter) {
      answering(letter).ifPresent(menu -> state = new Opened(menu));
    }
  }
}
