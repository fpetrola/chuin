# Autopsy 2026-11-14-plural-name-5 (plural-name)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `plural-name` that went away (written by cerco):

- `tui-api/src/main/java/com/fpetrola/tui/Keys.java|Keys`

## Cause

Keys was not a collection of keys but a second factory beside the toolkit: it made the named keys a program binds (Enter, Tab, Space, F1 to F12, Esc), while Toolkit already is the one object that makes every piece of the framework (points, areas, windows, views, menus). The plural named what it handed out, keys, because the role it played, making them, already had an owner and a name. The keyboard also took it only to build three of its keys, which it can build like all the others it reads.

model: knowledge-without-one-owner

## Context

- tui-api/src/main/java/com/fpetrola/tui/Toolkit.java:57 Toolkit#point, like area, window and the views: the toolkit is where a program gets what it builds with; the named keys now sit beside them at tui-api/src/main/java/com/fpetrola/tui/Toolkit.java:41.
- tui-text/src/main/java/com/fpetrola/tui/StreamKeyboard.java:41 the keyboard builds Space, Tab and Enter as constants, as it already built every other key it reads (arrows, Esc, function keys), instead of asking a Keys it was handed.
- tui-demo/src/main/java/com/fpetrola/tui/demo/Demo.java:78 a program binds F2 with toolkit.functionKey(2), the same object it builds its windows with.

## Same cause elsewhere

none: plural-name has nothing frozen any more; I looked for other factories beside Toolkit (grep for interfaces whose methods only return new values in tui-api) and found Toolkit alone.

## Principle

One owner per concept: making the pieces of the framework is the toolkit's job, so a second factory for some of them splits that knowledge and earns a name only by what it returns.

## Candidate rule

covered-by: plural-name
plural-name marked Keys for its plural name, and its message (a name for the role of the whole; a rename alone is no fix) is what led to the fix: the role was already named, Toolkit, so the named keys moved there and Keys went away instead of being renamed.
