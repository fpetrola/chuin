# Autopsy 2026-11-14-plural-name-3 (plural-name)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `plural-name` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java|TextRadioButtons`

## Cause

The group of options was named after how Turbo Vision's widget looks, several round buttons (TRadioButtons), and not after what it is in the model: a group of options of which exactly one is chosen. With the plural name the type read as a collection of buttons, so its behaviour was written as a collection's, choosing by position; the element, Option, had no behaviour until the kept-by-index fix. The name for the role of the whole was missing.

model: concept-without-a-class

## Context

- tui-api/src/main/java/com/fpetrola/tui/OptionGroup.java:4 the interface, whose javadoc already said "A group of options of which exactly one is chosen" while it was called RadioButtons.
- tui-demo/src/main/java/com/fpetrola/tui/demo/Demo.java:29 the demo's country group: what a caller builds is a group of options, which the new Toolkit#optionGroup says.
- tui-text/src/main/java/com/fpetrola/tui/TextOptionGroup.java:6 the implementation, whose options are now Place objects that choose themselves.

## Same cause elsewhere

- tui-api/src/main/java/com/fpetrola/tui/Keys.java:11 and tui-text/src/main/java/com/fpetrola/tui/Lines.java:4, the plural names still frozen by plural-name; each goes with its own rename proposal.

## Principle

A name says what a thing is, not what it looks like: a type is named for its role in the model (a group, a stack, a bar), so its behaviour follows the role.

## Candidate rule

covered-by: plural-name
plural-name marked TextRadioButtons for its plural name and a collection field, and its message (a name for the role of the whole; a rename alone is no fix) is what led here: first Option got the behaviour (kept-by-index, proposal 0057), then the whole was named for its role, OptionGroup, which the type already was.
