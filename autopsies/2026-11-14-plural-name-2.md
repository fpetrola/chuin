# Autopsy 2026-11-14-plural-name-2 (plural-name)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `plural-name` that went away (written by cerco):

- `tui-api/src/main/java/com/fpetrola/tui/RadioButtons.java|RadioButtons`

## Cause

The finding on RadioButtons went away because its only signal, the method choose(int), was removed by the kept-by-index fix; the name is still plural and is still Turbo Vision's TRadioButtons. What was missing is a name for the role of the whole: it is a group of options of which one is chosen, and the singular element, Option, now carries the behaviour. The plural survived because the interface was named after the widget's look (several round buttons) instead of what it is.

model: concept-without-a-class

## Context

- tui-api/src/main/java/com/fpetrola/tui/RadioButtons.java:4 the interface keeps its plural name with no int left; its own javadoc already says "A group of options of which exactly one is chosen".
- tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java:48 the Place inner class is now the Option that chooses itself; TextRadioButtons is still frozen by plural-name because it keeps a List field.

## Same cause elsewhere

- tui-text/src/main/java/com/fpetrola/tui/TextRadioButtons.java:10 the implementation, still frozen by plural-name.
- tui-api/src/main/java/com/fpetrola/tui/Keys.java and tui-text/src/main/java/com/fpetrola/tui/Lines.java:19 are the other frozen plural names; they go with their own rename proposals.

## Principle

A name says what a thing is, not what it looks like: a type is named for its role (a group, a stack, a bar), and a plural name is only right for a role that is itself a collection.

## Candidate rule

rule: frozen-name-kept
Detects a frozen plural-name finding that leaves the baseline while the type keeps the same plural name, because a signal went away and not because the name changed. It would tell the agent: "RadioButtons left the baseline but is still named in plural: the name was the finding; rename it to its role". Example: tui-api/src/main/java/com/fpetrola/tui/RadioButtons.java:4, which left plural-name's baseline when choose(int) was removed, still named RadioButtons.
