# Autopsy 2026-11-14-visitor-handed-parts-2 (visitor-handed-parts)

<!-- Findings went away. That is the symptom gone, not the cause understood. Read the code around the fix
and answer: what was missing from the model that let this shape appear, where else it shows, and what
rule would stop it next time. cerco stays red until every section is filled; then it files this form in
autopsies/ by itself. Places are written path:line, relative to the project root, and must exist. -->

## Symptom

Findings of `visitor-handed-parts` that went away (written by cerco):

- `tui-text/src/main/java/com/fpetrola/tui/TextButton.java|accept|visitor.button(this, caption, List.copyOf(listeners))`

## Cause

Pressing a button was something its visitors did, not something the button did: the Button interface only took listeners in, so the button handed out a copy of its listener list and two separate visitors, the one for Enter and the one for a click, each walked that list and called every listener themselves. The knowledge of what pressing means, telling every listener in order, lived outside the object that holds the listeners, twice.
model: behaviour-not-on-owner

## Context

- tui-text/src/main/java/com/fpetrola/tui/PressFocused.java:8 walked the handed listeners for Enter; now it only says button.press().
- tui-text/src/main/java/com/fpetrola/tui/ClickFocused.java:8 walked the same list again for a mouse click, the duplicated knowledge; now it also asks the button to press.
- tui-text/src/main/java/com/fpetrola/tui/TextButton.java:26 is where pressing lives now, next to the listeners it walks.

## Same cause elsewhere

- tui-text/src/main/java/com/fpetrola/tui/TextWindow.java:31 hands the visitor its title and area instead of itself, and TextWindow.java:19 hands each child's position as a visit of its own; Window is the last step of the migration (DECISIONS 28).
- The label had the same shape without the rule seeing it, since it handed only its text and not itself; it is fixed in this same step with visitLabel(Label).

## Principle

Information expert and tell, don't ask: the object that keeps the listeners is the one that knows how to notify them, so callers ask it to press rather than taking its listeners and notifying them from outside, which also leaves one place for that rule instead of two.

## Candidate rule

covered-by: visitor-handed-parts
The rule froze this accept because it handed the visitor its caption and a copy of its listeners next to itself; following it, visit only the button and ask it, is what moved pressing onto Button#press() and removed the second walk of the listeners in ClickFocused, so the existing rule already leads to the missing behaviour, not only to the symptom.
