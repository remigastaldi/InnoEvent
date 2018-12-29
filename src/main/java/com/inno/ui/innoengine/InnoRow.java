/*
 * File Created: Tuesday, 27th November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */

package com.inno.ui.innoengine;

import java.util.ArrayList;
import java.util.HashMap;

import com.inno.app.Core;
import com.inno.app.room.ImmutableSeat;
import com.inno.app.room.ImmutableSittingRow;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.engine.shape.InteractiveShape;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InnoRow {
  private Line _line = null;
  private InnoEngine _engine = null;
  private InteractiveShape<? extends Shape> _intShape = null;
  private ImmutableSittingSection _section = null;
  private ImmutableSittingRow _row = null;
  private HashMap<Integer, Circle> _seats = new HashMap<>();
  private Shape[] _text = new Shape[2];
  private ImmutableSeat _selectedSeat = null;
  private boolean _toParent = false;

  public InnoRow(InnoEngine engine, InteractiveShape<? extends Shape> shape, ImmutableSittingSection section,
      ImmutableSittingRow row, boolean toParent) {
    _engine = engine;
    _intShape = shape;
    _section = section;
    _row = row;
    _toParent = toParent;

    createAll();
  }

  private void createAll() {
    double[] vitalSpace = _engine.meterToPixel(new double[]{_section.getImmutableVitalSpace().getWidth(), _section.getImmutableVitalSpace().getHeight()});
    double[] start = null;
    double[] end = null;

    if (_toParent) {
      start = _intShape.noRotatedParentPointsToRotated(_engine.meterToPixel(_row.getPosStartRow()));
      end = _intShape.noRotatedParentPointsToRotated(_engine.meterToPixel(_row.getPosEndRow()));
    } else {
      start = _intShape.parentToLocal(_intShape.noRotatedParentPointsToRotated(_engine.meterToPixel(_row.getPosStartRow())));
      end =  _intShape.parentToLocal(_intShape.noRotatedParentPointsToRotated(_engine.meterToPixel(_row.getPosEndRow())));
    }

    _line = new Line(start[0], start[1], end[0], end[1]);
    _line.setStrokeWidth(3.5);

    if (_toParent)
      _engine.getPane().getChildren().add(_line);
    else
      _intShape.addAdditionalShape(_line);

    _line.setOnMouseReleased(event -> {
      selectRowSidebar();
    });

    createLabel(_toParent, vitalSpace);

    createSeats(_toParent);

    if (Core.get().getRowPrice(_section.getId(), _row.getIdRow()).getPrice() != -1) {
      setRowColor(Color.valueOf(Core.get().getRowPrice(_section.getId(), _row.getIdRow()).getColor()));
    } else {
      resetRowColor();
    }
  }

  private void createSeats(boolean toParent) {
	  ArrayList<? extends ImmutableSeat> seats = _row.getSeats();

    for (ImmutableSeat seat : seats) {
      double[] points = null;
      if (toParent) {
        points = _intShape.noRotatedParentPointsToRotated(
          new double[] { _engine.meterToPixel(seat.getPosition()[0]), _engine.meterToPixel(seat.getPosition()[1]) });
      } else {
        points = _intShape.parentToLocal(_intShape.noRotatedParentPointsToRotated(
          new double[] { _engine.meterToPixel(seat.getPosition()[0]), _engine.meterToPixel(seat.getPosition()[1]) }));
      }
      Circle circle = new Circle(points[0], points[1], 6d);
      circle.setFill(getDeriveColor(Color
          .valueOf(Core.get().getSeatPrice(_intShape.getID(), _row.getIdRow(), Integer.toString(seat.getId())).getColor())
          .deriveColor(1, 1, 1, 0.85)));
      circle.setStroke(Color.TRANSPARENT);
      circle.setStrokeWidth(2.5);
      circle.setOnMouseReleased(event -> {
        _selectedSeat = seat;
        _engine.getView().setSidebarFromFxmlFileName("sidebar_seat.fxml", this);
      });
      
      _seats.put(seat.getId(), circle);
      
      if (_toParent)
      _engine.getPane().getChildren().add(circle);
      else
      _intShape.addAdditionalShape(circle);
      
      if (Core.get().getSeatPrice(_section.getId(), _row.getIdRow(), Integer.toString(seat.getId())).getPrice() != -1) {
        setSeatColor(seat.getId(), Color.valueOf(Core.get().getSeatPrice(_section.getId(), _row.getIdRow(), Integer.toString(seat.getId())).getColor()));
      }
    }
  }

  private void createLabel(boolean toParent, double[] vitalSpace) {
    double[] endX = null;

    if (toParent)
      endX = _intShape.parentToLocal(new double[] { _line.getEndX() + 15, _line.getEndY() - vitalSpace[1] / 4 });
    else
      endX = new double[] { _line.getEndX() + 15, _line.getEndY() - vitalSpace[1] / 4 };

    Rectangle rect = new Rectangle(endX[0], endX[1], 10 + vitalSpace[1] / 4, vitalSpace[1] / 2);
    rect.setFill(Color.BLACK);
    rect.setOpacity(0.5);
    rect.setArcHeight(3);
    rect.setArcWidth(3);
    rect.setOnMouseReleased(event -> {
      System.out.println("Row " + _row.getIdRow());
      selectRowSidebar();
      _selectedSeat = null;
    });
    if (_engine.getSelectedShape() != _intShape)
      rect.setVisible(false);

    if (toParent) {
      // _engine.getPane().getChildren().add(rect);
      // rect.getTransforms().add(shape.getRotation());
    } else
      _intShape.addSelectShape(rect);

    if (toParent)
      endX = _intShape.parentToLocal(new double[]{_line.getEndX() + 20, _line.getEndY() + vitalSpace[1] / 10});
    else
      endX = new double[]{_line.getEndX() + 20, _line.getEndY() + vitalSpace[1] / 10};

    Text text = new Text(endX[0], endX[1], _row.getIdRow());
    text.setFill(Color.WHITE);
    text.setFont(new Font(vitalSpace[1] / 3));
    text.setOnMouseReleased(event -> {
      System.out.println("Row " + _row.getIdRow());
      selectRowSidebar();
      _selectedSeat = null;
    });
    if (_engine.getSelectedShape() != _intShape)
      text.setVisible(false);

    if (toParent) {
      // _engine.getPane().getChildren().add(text);
      // text.getTransforms().add(_intShape.getRotation());
    }
    else
      _intShape.addSelectShape(text);

    _text[0] = rect;
    _text[1] = text;
  }

  public void selectRowSidebar() {
    _engine.getView().setSidebarFromFxmlFileName("sidebar_row.fxml", this);
  }

  public ImmutableSittingSection getImmutableSection() {
    return _section;
  }

  public ImmutableSittingRow getImmutableRow() {
    return _row;
  }

  public ImmutableSeat getSelectedSeat() {
    return _selectedSeat;
  }

  public void resetRowColor() {
    _line.setStroke(getDeriveColor(Color.valueOf("#7289DA")));
  }

  public void resetSeatsColor() {
    for (Circle seat : _seats.values()) {
      seat.setStroke(Color.TRANSPARENT);
      seat.setFill(getDeriveColor(Color.valueOf("#FFA500")));
    }
  }

  public void resetSeatColor(int idSeat) {
    _seats.get(idSeat).setFill(getDeriveColor(Color.valueOf("#FFA500")));
    _seats.get(idSeat).setStroke(Color.TRANSPARENT);
    for (Circle seat : _seats.values()) {
      if (seat != _seats.get(idSeat) && seat.getStroke() != Color.TRANSPARENT) {
        seat.setFill(seat.getStroke());
      }
    }
  }

  public Color getDeriveColor(Color color) {
    return color.deriveColor(1, 1, 1, 0.9);
  }

  public void setRowColor(Color color) {
    _line.setStroke(getDeriveColor(color));
    _intShape.setColor(getDeriveColor(Color.valueOf("#6378bf")));
    for (Circle seat : _seats.values()) {
      seat.setStroke(getDeriveColor(color));
      seat.setFill(getDeriveColor(Color.valueOf("#FFA500")));
    }
  }

  public void setSeatColor(int idSeat, Color color) {
    resetRowColor();
    _intShape.setColor(getDeriveColor(Color.valueOf("#6378bf")));
    _seats.get(idSeat).setFill(getDeriveColor(color));
    _seats.get(idSeat).setStroke(getDeriveColor(color));
    for (Circle seat : _seats.values()) {
      if (seat.getStroke() != Color.TRANSPARENT) {
        seat.setFill(seat.getStroke());
      }
    }
  }

  public void destroy() {
    if (_toParent)
      _engine.getPane().getChildren().remove(_line);
    else
      _intShape.removeAdditionalShape(_line);
    
    for (Circle seat : _seats.values()) {
      if (_toParent)
        _engine.getPane().getChildren().remove(seat);
      else
        _intShape.removeAdditionalShape(seat);
    }

    if (_toParent) {
      _engine.getPane().getChildren().remove(_text[0]);
      _engine.getPane().getChildren().remove(_text[1]);
    } else {
      _intShape.removeSelectShape(_text[0]);
      _intShape.removeSelectShape(_text[1]);
    }
  }

  public void updateRowFromData() {
    destroy();
    createAll();
  }
}