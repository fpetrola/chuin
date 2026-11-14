package com.fpetrola.tui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
    View view = ok;
    window.place(area);
    window.add(view, position);

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

  @Test
  void aNewCheckBoxIsUncheckedAndTogglingItMakesItChecked() {
    CheckBox remember = toolkit.checkBox("Recordar");
    ArgumentCaptor<Mark> mark = ArgumentCaptor.forClass(Mark.class);
    ArgumentCaptor<Runnable> toggle = ArgumentCaptor.forClass(Runnable.class);
    MarkVisitor marks = mock(MarkVisitor.class);

    remember.accept(visitor);
    verify(visitor).checkBox(eq(remember), eq("Recordar"), mark.capture(), toggle.capture());
    mark.getValue().accept(marks);
    toggle.getValue().run();
    remember.accept(visitor);
    verify(visitor, times(2)).checkBox(eq(remember), eq("Recordar"), mark.capture(), any(Runnable.class));
    mark.getValue().accept(marks);

    InOrder order = inOrder(marks);
    order.verify(marks).unchecked();
    order.verify(marks).checked();
  }
}
