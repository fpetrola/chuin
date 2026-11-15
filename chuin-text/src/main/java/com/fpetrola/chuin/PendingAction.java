package com.fpetrola.chuin;

/** What is left to do once a walk of the views is over: what was noted last, or what was due from the start. */
class PendingAction implements Settle {
  private Runnable due;

  PendingAction(Runnable otherwise) {
    this.due = otherwise;
  }

  @Override
  public void note(Runnable does) {
    due = does;
  }

  @Override
  public void settle() {
    due.run();
  }
}
