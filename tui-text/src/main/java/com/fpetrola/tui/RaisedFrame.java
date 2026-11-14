package com.fpetrola.tui;

/** A frame that stands over what is under it: the frame itself and the shadow it casts. Windows and open menus are drawn this way. */
class RaisedFrame implements Drawing {
  private final Drawing frame;
  private final Drawing shadow;

  RaisedFrame(Area area, String title, char tint) {
    frame = new Frame(area, title, tint);
    shadow = new Shadow(area);
  }

  @Override
  public void drawOn(Screen screen) {
    frame.drawOn(screen);
    shadow.drawOn(screen);
  }
}
