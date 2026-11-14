# Autopsy 2026-11-14-one-accept (one-accept)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `one-accept` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/TextListBox.java|TextListBox`

## Cause

The list box's own accept only announced the box, and its rows and scroll bar were reached through a second door, acceptRows with a visitor of their own, that the painter had to know about and call. The structure of the list, how many rows it shows, which item each row holds and what scroll bar cell goes beside it, was walked at the visitor's request instead of by the list when it is visited, so whoever visited a list had to know it has rows.
model: structure-walked-from-outside

## Context

- tui-text/src/main/java/com/fpetrola/tui/TextListBox.java:45 is now the one accept, visiting the box and then each row and cell itself.
- tui-text/src/main/java/com/fpetrola/tui/Painter.java:119 used to call box.acceptRows with a row painter; now it only sets up the row painter, and the rows reach it as ordinary visits, Painter.java:125 onward.
- tui-text/src/main/java/com/fpetrola/tui/RowPainter.java:10 used to implement the list's separate row visitor; it now handles those visits as part of ViewVisitor.

## Same cause elsewhere

- tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java:32 has acceptOptions next to its accept at TextRadioButtons.java:44, and the painter calls it at Painter.java:166; it is the next step (DECISIONS 39).
- tui-text/src/main/java/com/fpetrola/tui/TextMenuBar.java:15 and TextMenu.java:27 are walked from MenuDesktop.java:59 and MenuDesktop.java:65 and MenuPainter.java:47; the rule does not see them because each type has only one accept-like method, but the menus are walked from outside the same way.

## Principle

In a visitor, only the element knows its structure: its single accept visits it and then has the visitor accept on its parts, and a visitor that does not care about a part simply does nothing when it meets it, instead of choosing which parts to walk.

## Candidate rule

covered-by: one-accept
The rule froze TextListBox for having a second accept-like method taking a visitor, and the way out it names, one accept that walks its parts, is exactly the missing model: the list now walks its own rows. The menus show the same cause without a second accept in one type; walked-by-the-visitor, still a candidate in this cerco, is the one that would see MenuPainter calling menu.acceptItems from inside a visitor.
