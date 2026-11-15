package com.fpetrola.chuin.text.view;

import com.fpetrola.chuin.text.TextToolkit;
import com.fpetrola.chuin.Toolkit;
import com.fpetrola.chuin.view.Label;
import com.fpetrola.chuin.view.ViewVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GroupingViewsTest {
  private final Toolkit toolkit = new TextToolkit();

  /** A group keeps the views placed in it, visits them in the order added, and reaches as far as its farthest view. */
  @Test
  void aGroupVisitsItsViewsInOrderAndIsAsBigAsItsFarthestView() {
    Group group = new TextGroup();
    StringBuilder met = new StringBuilder();
    group.add(toolkit.label("uno"), toolkit.point(4, 0));
    group.add(toolkit.label("dos"), toolkit.point(0, 2));

    group.accept(new ViewVisitor() {
      @Override
      public void visitLabel(Label label) {
        met.append(label.text()).append(' ');
      }
    });

    assertEquals("uno dos ", met.toString());
    assertEquals("7x3", group.size().columns() + "x" + group.size().rows());
  }
}
