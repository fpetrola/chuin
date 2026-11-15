# Autopsy 2026-11-14-kept-by-index (kept-by-index)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `kept-by-index` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/TextListBox.java|TextListBox`
- `tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java|TextRadioButtons`

## Cause

The options of a group and the rows of a list were already handed to visitors as Option and ListItem, but as records that only carried text and their owner: they could not do anything. So whoever wanted to choose one (a click, a letter) had to count the visits to learn its position and hand that position back to the group or the list, which then checked it against its size. The element was visited as an object but acted on as a number, because its behaviour stayed on the collection.

model: behaviour-not-on-owner

## Context

- tui-text/src/main/java/com/fpetrola/tui/ClickFocused.java:100 now notes `option::choose` on the clicked row; before, visitRadioButtons noted `group.choose(row)` with the row of the click as the option's position.
- tui-text/src/main/java/com/fpetrola/tui/ReachByLetter.java:96 now asks the option for its group and chooses it; before, it kept the group met last and a counter of options to pass `group.choose(place)`.
- tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java:48 and tui-text/src/main/java/com/fpetrola/tui/TextListBox.java:87: each element is an inner class that knows its place, as TextDropDown's Item did when DropDownItem#choose was added.

## Same cause elsewhere

none left of this kind: I looked for non-private methods taking an int in tui-api and tui-text (grep for `(int ` in interfaces); what remains is Keys#function(int), a key's own number, and View geometry (Toolkit#point, area), values rather than positions. The positions that remain are private to their owner: TextListBox.java:16 `selected` and TextRadioButtons.java:10 `chosen`.

## Principle

Tell, don't ask: the element that a person points at (an option, a row) is told to choose itself; nobody counts positions to tell the collection which one.

## Candidate rule

covered-by: kept-by-index
The rule marked TextListBox#selectShown and TextRadioButtons#choose, and what it said (callers keep a position and hand it back: give the element the behaviour and hand it out) is exactly the fix made: Option#choose and ListItem#select on the elements the collections already handed out, with the int methods removed.
