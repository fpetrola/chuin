package com.fpetrola.tui;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class VisitingViewsTest {
  private final Toolkit toolkit = new TextToolkit();
  private final ViewVisitor visitor = mock(ViewVisitor.class);

  @Test
  void aWindowHandsItsTitleAreaAndPlacedButtonsToTheVisitor() {
    Window window = toolkit.window("Saludo");
    Area area = toolkit.area(0, 0, 20, 5);
    Point position = toolkit.point(1, 1);
    Button ok = toolkit.button("OK");
    window.place(area);
    window.add(ok, position);

    window.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).window("Saludo", area);
    order.verify(visitor).child(position);
    order.verify(visitor).button(ok, "OK", List.of());
  }

  @Test
  void aButtonHandsItselfItsCaptionAndItsListenersToTheVisitor() {
    Button ok = toolkit.button("OK");
    ButtonListener listener = mock(ButtonListener.class);
    ok.onPress(listener);

    ok.accept(visitor);

    verify(visitor).button(ok, "OK", List.of(listener));
  }

  @Test
  void aLabelHandsItsCurrentTextToTheVisitor() {
    Label message = toolkit.label("antes");
    message.text("después");

    message.accept(visitor);

    verify(visitor).label("después");
  }

  @Test
  void aWindowHandsItsPlacedLabelsToTheVisitor() {
    Window window = toolkit.window("Saludo");
    Point position = toolkit.point(1, 2);
    window.add(toolkit.label("hola"), position);

    window.accept(visitor);

    InOrder order = inOrder(visitor);
    order.verify(visitor).child(position);
    order.verify(visitor).label("hola");
  }
}
