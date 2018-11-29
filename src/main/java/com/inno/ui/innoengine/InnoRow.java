/*
 * File Created: Tuesday, 27th November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

import java.util.ArrayList;

import com.inno.app.room.ImmutableSeat;
import com.inno.app.room.ImmutableSittingRow;
import com.inno.ui.engine.shape.InteractiveShape;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class InnoRow {
  private Line _line = null;
  private Circle[] _seats = null;
  private InnoEngine _engine = null;

  public InnoRow(InnoEngine engine, InteractiveShape<? extends Shape> shape, ImmutableSittingRow row, double vitalSpace) {
    _engine = engine;
  
    double[] start =  _engine.meterToPixel(row.getPosStartRow());
    double[] end = _engine.meterToPixel(row.getPosEndRow());
    _line = new Line(start[0], start[1], end[0], end[1]);
    _line.setStroke(Color.DARKBLUE);
    _line.setStrokeWidth(vitalSpace / 2);

    shape.addAdditionalShape(_line);

    _line.setOnMouseClicked(event -> {
      // engine.getView().setSidebarFromFxmlFileName("sidebar_row.fxml", this);
    });

    int i = 0;
    ArrayList<? extends ImmutableSeat> seats = row.getSeats();
    _seats = new Circle[seats.size()];
    for (ImmutableSeat seat : seats) {
      Circle circle = new Circle(_engine.meterToPixel(seat.getPosition()[0]), _engine.meterToPixel(seat.getPosition()[1]), vitalSpace / 4, Color.ORANGE);

      circle.setOnMouseClicked(event -> {
        engine.getView().setSidebarFromFxmlFileName("sidebar_seat.fxml", this);

      });
      _seats[i] = circle;
      shape.addAdditionalShape(circle);
      ++i;
    }
  }
}