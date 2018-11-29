/*
 * File Created: Tuesday, 27th November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 29th November 2018
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
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    _line.setStrokeWidth(vitalSpace / 4);

    shape.addAdditionalShape(_line);
    _line.setOnMouseClicked(event -> {
      // engine.getView().setSidebarFromFxmlFileName("sidebar_row.fxml", this);
    });

    Rectangle rect = new Rectangle(_line.getEndX() + vitalSpace / 4, _line.getEndY() - vitalSpace / 4, vitalSpace, vitalSpace / 2);
    // rect.setStroke(Color.DARKSLATEGRAY);
    rect.setFill(Color.BLACK);
    rect.setOpacity(0.5);
    // rect.setStroke(Color.ORANGE);
    rect.setArcHeight(3);
    rect.setArcWidth(3);
    rect.setOnMouseClicked(event -> {
      System.out.println("Row " + row.getIdRow());
    });
    shape.addSelectShape(rect);

    Text text = new Text(_line.getEndX() + vitalSpace / 2, _line.getEndY() + vitalSpace / 6, row.getIdRow());
    text.setFill(Color.WHITE);
    text.setFont(new Font(8));
    text.setOnMouseClicked(event -> {
      System.out.println("Row " + row.getIdRow());
    });
    shape.addSelectShape(text);



    int i = 0;
    ArrayList<? extends ImmutableSeat> seats = row.getSeats();
    _seats = new Circle[seats.size()];
    for (ImmutableSeat seat : seats) {
      Circle circle = new Circle(_engine.meterToPixel(seat.getPosition()[0]), _engine.meterToPixel(seat.getPosition()[1]), vitalSpace / 3, Color.ORANGE);

      circle.setOnMouseClicked(event -> {
        engine.getView().setSidebarFromFxmlFileName("sidebar_seat.fxml", this);

      });
      _seats[i] = circle;
      shape.addAdditionalShape(circle);
      ++i;
    }
  }
}