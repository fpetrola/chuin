package com.fpetrola.tui;

/** Meets each view placed in a window together with where it was placed. */
interface ChildVisitor {
  void child(View view, Point position);
}
