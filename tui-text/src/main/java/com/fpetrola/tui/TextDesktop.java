package com.fpetrola.tui;

import com.fpetrola.tui.geometry.Area;
import com.fpetrola.tui.geometry.Point;
import com.fpetrola.tui.geometry.Size;
import com.fpetrola.tui.input.Key;
import com.fpetrola.tui.input.KeyVisitor;
import com.fpetrola.tui.screen.Screen;
import com.fpetrola.tui.view.ScrollPanel;
import com.fpetrola.tui.view.View;
import com.fpetrola.tui.view.ViewVisitor;
import com.fpetrola.tui.view.Window;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

class TextDesktop implements Windows {
  private final List<Window> opened = new ArrayList<>();
  /** A focus no view has: windows below the top one are drawn with it. */
  private static final int UNFOCUSED = -1;
  private static final Runnable NOTHING = () -> { };
  private static final Dragging NOT_DRAGGING = cell -> { };
  /** F6 raises the bottom window over the others, as in Turbo Vision. */
  private static final int NEXT_WINDOW = 6;
  /** Ctrl+W closes the top window. */
  private static final char CLOSE_WINDOW = 'w';
  /** The smallest a window shrinks to: its frame with one cell inside. */
  private static final int FEWEST_COLUMNS = 3;
  private static final int FEWEST_ROWS = 3;
  private int focused;
  /** What a drag does: move the window taken by its title, or nothing. */
  private Dragging dragging = NOT_DRAGGING;
  /** What a click on the top window's frame may fall on, as it was last drawn: its scroll bars. */
  private final List<ClickTarget> onTopTargets = new ArrayList<>();
  /** Where the focusable views of the top window were last drawn, in focus order. */
  private final List<Area> onTop = new ArrayList<>();
  /** The focus each window had when it last left the top, by its place in {@link #opened}. */
  private final List<Integer> remembered = new ArrayList<>();

  @Override
  public void open(Window window) {
    if (!opened.isEmpty()) {
      remembered.set(opened.size() - 1, focused);
    }
    opened.add(window);
    remembered.add(0);
    focused = 0;
  }

  @Override
  public void type(Key key) {
    key.accept(new Typed());
    scrollToFocus();
  }

  /** A click on an item of the focused drop down's open list chooses it; anywhere else it clicks as usual. */
  @Override
  public void click(Point cell) {
    Area field = focused < onTop.size() ? onTop.get(focused) : new TextArea(0, 0, 0, 0);
    Settle pending = new PendingAction(() -> clickAsUsual(cell));
    ChooseFromOpenList choose = new ChooseFromOpenList(cell.column() - field.column(), cell.row() - field.row(), pending);
    top().ifPresent(window -> window.accept(new AtFocus(focused, choose)));
    pending.settle();
  }

  /** Closes any open list; raises the window under the cell, or hands the click to the top window's bars, then to the window. */
  private void clickAsUsual(Point cell) {
    closeOpenList();
    int under = windowUnder(cell);
    boolean below = under >= 0 && under < opened.size() - 1;
    if (below) {
      raise(under);
      top().filter(window -> cell.row() == window.area().row()).ifPresent(window -> grab(window, cell));
    } else {
      Runnable chain = () -> clickOnTop(cell, under);
      for (ClickTarget target : onTopTargets.reversed()) {
        Runnable next = chain;
        chain = () -> target.click(cell, next);
      }
      chain.run();
    }
  }

  /** A click on the top window, past its scroll bars: its title takes it, or its corner, or the view under it. */
  private void clickOnTop(Point cell, int under) {
    top().filter(window -> under >= 0 && cell.row() == window.area().row()).ifPresent(window -> grab(window, cell));
    top().filter(window -> under == opened.size() - 1 && atCorner(window.area(), cell)).ifPresent(this::stretch);
    for (int view = 0; view < onTop.size(); view++) {
      Area area = onTop.get(view);
      boolean across = cell.column() >= area.column() && cell.column() < area.column() + area.columns();
      boolean down = cell.row() >= area.row() && cell.row() < area.row() + area.rows();
      if (across && down) {
        focused = view;
        int clicked = view;
        Settle pending = new PendingAction(NOTHING);
        ClickFocused click = new ClickFocused(cell.column() - area.column(), cell.row() - area.row(), pending,
            held -> dragging = to -> held.to(new TextPoint(to.column() - area.column(), to.row() - area.row())));
        top().ifPresent(window -> window.accept(new AtFocus(clicked, click)));
        pending.settle();
      }
    }
  }

  @Override
  public void drag(Point cell) {
    dragging.to(cell);
  }

  @Override
  public void release(Point cell) {
    dragging = NOT_DRAGGING;
    boolean over = focused < onTop.size() && within(onTop.get(focused), cell);
    Settle pending = new PendingAction(NOTHING);
    top().ifPresent(window -> window.accept(new AtFocus(focused, new LetGo(over, pending))));
    pending.settle();
  }

  private static boolean within(Area area, Point cell) {
    boolean across = cell.column() >= area.column() && cell.column() < area.column() + area.columns();
    boolean down = cell.row() >= area.row() && cell.row() < area.row() + area.rows();
    return across && down;
  }

  /** Takes a window by its title at a cell: dragging moves it so that cell stays under the pointer. */
  private void grab(Window window, Point held) {
    Area from = window.area();
    int across = held.column() - from.column();
    int down = held.row() - from.row();
    dragging = to -> window.place(new TextArea(to.column() - across, to.row() - down, from.columns(), from.rows()));
  }

  private static boolean atCorner(Area area, Point cell) {
    return cell.column() == area.column() + area.columns() - 1 && cell.row() == area.row() + area.rows() - 1;
  }

  /** Takes a window by its bottom-right corner: dragging puts the corner under the pointer, the top left staying. */
  private void stretch(Window window) {
    Area from = window.area();
    dragging = to -> window.place(new TextArea(from.column(), from.row(),
        Math.max(FEWEST_COLUMNS, to.column() - from.column() + 1), Math.max(FEWEST_ROWS, to.row() - from.row() + 1)));
  }

  @Override
  public void escape(Runnable otherwise) {
    Settle pending = new PendingAction(otherwise);
    top().ifPresent(window -> window.accept(new AtFocus(focused, new CloseOpenList(pending))));
    pending.settle();
  }

  /** Closes the focused drop down's open list, if there is one. */
  private void closeOpenList() {
    Settle pending = new PendingAction(NOTHING);
    top().ifPresent(window -> window.accept(new AtFocus(focused, new CloseOpenList(pending))));
    pending.settle();
  }

  /**
   * Scrolls the top window's panel so the focused view comes into sight when its first cell is out of it. Where the
   * view is and how big come from its placement; what shows, from the panel's origin and size.
   */
  private void scrollToFocus() {
    top().ifPresent(window -> {
      Map<View, Area> placed = new LinkedHashMap<>();
      List<ScrollPanel> panels = new ArrayList<>();
      window.accept(new CollectFocusable(placed));
      window.accept(new ViewVisitor() {
        @Override
        public void visitScrollPanel(ScrollPanel panel) {
          panels.add(panel);
        }
      });
      List<Area> at = List.copyOf(placed.values());
      if (focused < at.size() && !panels.isEmpty()) {
        ScrollPanel panel = panels.getFirst();
        Area view = at.get(focused);
        Size shown = panel.size();
        int columns = intoSight(view.column() - panel.origin().column(), view.columns(), shown.columns() - 1);
        int rows = intoSight(view.row() - panel.origin().row(), view.rows(), shown.rows() - 1);
        panel.scroll(columns, rows);
      }
    });
  }

  /**
   * How far to scroll so a view whose first cell is out of sight comes into it, showing as much of the view as fits;
   * nothing when its first cell already shows, even if the rest is cut off. Counted from the inside's first cell.
   */
  private static int intoSight(int start, int length, int inside) {
    int past = Math.min(start, start + length - inside);
    return start < 0 ? start : start >= inside ? past : 0;
  }

  @Override
  public void draw(Screen screen) {
    List<Drawing> popups = new ArrayList<>();
    clear(screen);
    onTop.clear();
    int last = opened.size() - 1;
    for (int at = 0; at < opened.size(); at++) {
      List<Area> drawn = at == last ? onTop : new ArrayList<>();
      List<ClickTarget> targets = at == last ? onTopTargets : new ArrayList<>();
      targets.clear();
      opened.get(at).accept(at == last ? new Painter(screen, focused, drawn, Line.DOUBLE, popups, targets, held -> dragging = held) : new Painter(screen, UNFOCUSED, drawn, Line.SINGLE, popups, targets, held -> dragging = held));
    }
    popups.forEach(popup -> popup.drawOn(screen));
  }

  /** Where among the opened windows is the topmost one covering this cell, or -1 when the cell is bare desktop. */
  private int windowUnder(Point cell) {
    for (int at = opened.size() - 1; at >= 0; at--) {
      Area area = opened.get(at).area();
      boolean across = cell.column() >= area.column() && cell.column() < area.column() + area.columns();
      boolean down = cell.row() >= area.row() && cell.row() < area.row() + area.rows();
      if (across && down) {
        return at;
      }
    }
    return -1;
  }

  /** Brings the window at this place over the others, giving it back the focus it had and keeping the top's. */
  private void raise(int at) {
    remembered.set(opened.size() - 1, focused);
    opened.add(opened.remove(at));
    remembered.add(remembered.remove(at));
    focused = remembered.get(opened.size() - 1);
  }

  /** Hands ↑ or ↓ to the focused view; when there is none, the window moves. */
  private void arrow(BinaryOperator<Runnable> choose, Runnable otherwise) {
    List<View> focusable = new ArrayList<>();
    top().ifPresent(window -> window.accept(new CollectFocusable(focusable)));
    if (focused < focusable.size()) {
      focusable.get(focused).accept(new ListArrow(choose, otherwise));
    } else {
      otherwise.run();
    }
  }

  /** Blanks every cell and takes its tint away, so nothing drawn before is left behind. */
  private void clear(Screen screen) {
    screen.hideCursor();
    List<String> lines = screen.lines();
    for (int row = 0; row < lines.size(); row++) {
      int columns = lines.get(row).length();
      screen.write(0, row, " ".repeat(columns));
      TintRole.BARE.paint(screen, 0, row, columns);
    }
  }

  private Optional<Window> top() {
    return opened.stream().reduce((below, above) -> above);
  }

  /** Moves the top window by the given cells, wherever that leaves it: the screen cuts what falls outside. */
  private void move(int columns, int rows) {
    top().ifPresent(window -> window.accept(new MoveWindow(columns, rows)));
  }

  /** What each key does to the desktop: Tab moves the focus, Enter presses the focused button, Space toggles the focused check box, a letter goes into the focused input line. */
  private class Typed implements KeyVisitor {
    @Override
    public void enter() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new PressFocused())));
    }

    /** Moves the focus to the next focusable view, back to the first after the last. */
    @Override
    public void tab() {
      closeOpenList();
      List<View> focusable = new ArrayList<>();
      top().ifPresent(window -> window.accept(new CollectFocusable(focusable)));
      focused = (focused + 1) % Math.max(1, focusable.size());
    }

    @Override
    public void space() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new ToggleFocused())));
    }

    @Override
    public void character(char typed) {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new TypeIntoFocused(typed))));
    }

    @Override
    public void backspace() {
      top().ifPresent(window -> window.accept(new AtFocus(focused, new EraseFocused())));
    }

    @Override
    public void up() {
      arrow((previous, next) -> previous, () -> move(0, -1));
    }

    @Override
    public void down() {
      arrow((previous, next) -> next, () -> move(0, 1));
    }

    @Override
    public void left() {
      move(-1, 0);
    }

    @Override
    public void right() {
      move(1, 0);
    }

    @Override
    public void alt(char letter) {
      List<View> focusable = new ArrayList<>();
      top().ifPresent(window -> window.accept(new CollectFocusable(focusable)));
      top().ifPresent(window -> window.accept(new ReachByLetter(letter, focusable, view -> focused = view)));
    }

    @Override
    public void control(char letter) {
      if (letter == CLOSE_WINDOW && !opened.isEmpty()) {
        opened.remove(opened.size() - 1);
        remembered.remove(remembered.size() - 1);
        focused = opened.isEmpty() ? 0 : remembered.get(opened.size() - 1);
      }
    }

    @Override
    public void function(int number) {
      if (number == NEXT_WINDOW && opened.size() > 1) {
        raise(0);
      }
    }

    @Override
    public void other() {
    }
  }
}
