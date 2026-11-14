package com.fpetrola.tui;

/** An option of a group of radio buttons, with the group it belongs to. */
record TextOption(RadioButtons group, String text) implements Option {
}
