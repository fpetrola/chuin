package com.fpetrola.chuin.text.pointer;

/** What is left to do once a walk of the views is over: what was noted last, or what was due from the start. */
public class PendingAction implements Settle {
  private Runnable due;

  public PendingAction(Runnable otherwise) {
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
