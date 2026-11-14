# Autopsy 2026-11-14-visitor-handed-parts (visitor-handed-parts)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `visitor-handed-parts` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/TextCheckBox.java|accept|visitor.checkBox(this, text, mark, this::toggle)`

## Cause

The check box had no behaviour or state of its own in the API: CheckBox was an empty interface, so the only way for the painter, the desktop and the key actions to reach its text, its mark and how to toggle it was for the check box to take itself apart and hand every piece to the visitor in one call. The visitor was used as a destructuring channel instead of a double dispatch, which is why ViewVisitor grew one long signature per view and every new piece of state changed all visitors.
model: behaviour-not-on-owner

## Context

- tui-api/src/main/java/com/fpetrola/tui/ViewVisitor.java:20 now visits the CheckBox alone; before, its checkBox method took the text, the mark and a toggle Runnable next to the box.
- tui-text/src/main/java/com/fpetrola/tui/ToggleFocused.java:8 used to run the Runnable it was handed; now it asks the box to toggle, the owner of that state.
- tui-text/src/main/java/com/fpetrola/tui/Painter.java:79 used to receive the Mark; now it asks the box to tell its mark through a nested MarkVisitor.

## Same cause elsewhere

- tui-text/src/main/java/com/fpetrola/tui/TextButton.java:21 hands the visitor its caption and a copy of its listeners next to itself; Button is next in the migration (DECISIONS 28).
- tui-text/src/main/java/com/fpetrola/tui/TextLabel.java:17 hands only its text, without itself; same missing behaviour on Label.
- tui-text/src/main/java/com/fpetrola/tui/TextWindow.java:31 hands its title and area, and TextWindow.java:19 hands each child's position as a separate visit; Window is the last step.

## Principle

Tell, don't ask, and a visitor is double dispatch: each view owns its state and behaviour and is visited as itself; whoever needs more asks the view, or the view walks its insides through a nested visitor, instead of the view handing its parts over.

## Candidate rule

covered-by: visitor-handed-parts
The rule froze exactly this call when it arrived in cerco 0.25.0 and named what it handed over next to `this`; what it told, receive only the view and ask it, is what led to giving CheckBox text(), toggle() and acceptMark(MarkVisitor), so the rule already points at the missing behaviour on the owner, not only at the symptom.
