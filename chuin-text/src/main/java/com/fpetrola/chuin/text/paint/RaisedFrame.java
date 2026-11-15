package com.fpetrola.chuin.text.paint;

import com.fpetrola.chuin.text.screen.Tint;
import com.fpetrola.chuin.geometry.Area;
import com.fpetrola.chuin.screen.Screen;


/** A frame that stands over what is under it: the frame itself and the shadow cast by the same area. Windows and open menus are drawn this way. */
public class RaisedFrame implements Drawing {
  private final Drawing frame;
  private final Drawing shadow;

  public RaisedFrame(Area area, String title, Tint tint, Border border) {
    this.frame = new Frame(area, title, tint, border);
    this.shadow = new Shadow(area);
  }

  @Override
  public void drawOn(Screen screen) {
    frame.drawOn(screen);
    shadow.drawOn(screen);
  }
}
