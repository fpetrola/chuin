# Autopsy 2026-11-14-plural-name-4 (plural-name)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `plural-name` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/Lines.java|Lines`

## Cause

The enum was named after the set of its values ("the lines a frame can be drawn with") instead of after what each value is: one kind of line, single or double, that a frame is drawn with. An enum's name is read at every use with a value after it, so the plural made each use read as a collection, Lines.DOUBLE, when the code means one line style. Nothing in the model was missing beyond the name: each value already carries its own corners and strokes and draws a border by itself.

model: value-without-a-type

## Context

- tui-text/src/main/java/com/fpetrola/tui/TextDesktop.java:136 picks Line.DOUBLE for the window on top and Line.SINGLE for the rest: each use names one line style.
- tui-text/src/main/java/com/fpetrola/tui/Frame.java:10 takes a Border, which each value of the enum is; a frame is drawn with one line, never with "lines".
- tui-text/src/main/java/com/fpetrola/tui/DropDownList.java:42 the open list's frame, drawn with Line.SINGLE.

## Same cause elsewhere

- tui-api/src/main/java/com/fpetrola/tui/Keys.java:11, the last plural name frozen by plural-name; it is a factory of named keys, not an enum, and goes with its own proposal.

## Principle

An enum is named for what one of its values is, so every use reads as that value: Line.DOUBLE is a line, as Color.RED is a color.

## Candidate rule

covered-by: plural-name
plural-name marked Lines because it is an enum with a plural name, and its message names the fix for that case, an enum in singular, which is what was done: Line, whose values SINGLE and DOUBLE each are one line style.
