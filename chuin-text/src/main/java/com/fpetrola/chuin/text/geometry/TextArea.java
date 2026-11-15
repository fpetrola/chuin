package com.fpetrola.chuin.text.geometry;

import com.fpetrola.chuin.geometry.Area;

public record TextArea(int column, int row, int columns, int rows) implements Area {
}
