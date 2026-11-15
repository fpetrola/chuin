# Autopsy 2026-11-14-plural-name (plural-name)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `plural-name` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/MenuTitles.java|MenuTitles`

## Cause

The menu bar had no object for "a menu's title where the bar puts it". The layout was computed as a list of start columns and every question about it (which title is under this cell, which answers this letter, what are the rows of this menu) was answered by an index into that list. The index then travelled: the Opened state kept `int open` and the key visitors passed it back. A plural holder appeared because the singular element that should carry the behaviour never existed; MenuTitles was only the place where the index lookups were gathered when MenuDesktop grew too long.

model: concept-without-a-class

## Context

- tui-text/src/main/java/com/fpetrola/tui/MenuDesktop.java:218 now holds the open title as a PlacedTitle; before the redesign (8640508) it was `private final int open`, and the left/right keys did `(open + 1) % count`, arithmetic on positions instead of asking a title for its neighbour.
- tui-text/src/main/java/com/fpetrola/tui/MenuDesktop.java:120 walks the bar with a MenuVisitor and places each title where the one before ends: the bar gives its menus in order, so the start column is a fact of the walk, not something to store in a list.
- tui-text/src/main/java/com/fpetrola/tui/MenuPainter.java:43 draws each title through an EntryRow that advances a column as captions are put; it is the other place that knows where a title sits.

## Same cause elsewhere

- tui-text/src/main/java/com/fpetrola/tui/MenuPainter.java:43: where a title sits is computed again by the painter's EntryRow; the placed title and the painter should share one owner of that placement (next fix).
- tui-api/src/main/java/com/fpetrola/tui/RadioButtons.java:16 `choose(int option)` and tui-api/src/main/java/com/fpetrola/tui/ListBox.java:28 `selectShown(int row)`: a click hands an index where an Option or a shown row could choose itself, as DropDownItem#choose now does.
- tui-api/src/main/java/com/fpetrola/tui/Keys.java:11 `function(int number)` is a plural factory frozen by plural-name; there the int is the key's own number, a value, not a position, so it is a naming case rather than this cause.

## Principle

Tell, don't ask, and every concept of the domain is an object: an element of a collection that has behaviour of its own (being under a cell, answering a letter, showing its items) is an object the collection hands out or walks, never a position others keep and pass back.

## Candidate rule

rule: kept-by-index
Detects a type whose non-private methods take an int that indexes a collection it keeps and return parts of that element, or return an index (int or OptionalInt) that callers pass back to it. It would tell the agent: "callers keep a position and hand it back: the element at that position is missing as an object; give it the behaviour and hand it out". Example: the removed tui-text/src/main/java/com/fpetrola/tui/MenuTitles.java (menu(int), start(int), rows(int), items(int), titleAt returning OptionalInt), and today tui-api/src/main/java/com/fpetrola/tui/RadioButtons.java:16. cerco-f2 named it as a candidate when it shipped plural-name, which catches the name but not a renamed type that still works by index.
