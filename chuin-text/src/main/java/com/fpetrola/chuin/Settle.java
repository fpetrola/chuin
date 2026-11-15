package com.fpetrola.chuin;

/**
 * Something a visit leaves pending until it is over: the visitor notes what to do as it meets the views, and whoever
 * started the walk settles it once every view was met, so nothing done changes what is still being walked.
 */
interface Settle {
  /** Notes what to do once every view was met, in place of what was noted before. */
  void note(Runnable does);

  void settle();
}
