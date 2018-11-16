/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;

import java.util.ArrayList;

import com.inno.ui.engine.shape.InteractivePolygon;
import com.inno.ui.engine.shape.InteractiveRectangle;
import com.inno.ui.engine.shape.InteractiveShape;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import  javafx.event.EventType;
import  javafx.event.EventHandler;
import  javafx.scene.input.MouseEvent;
import  javafx.scene.shape.Rectangle;
import  javafx.scene.shape.Shape;
import  javafx.scene.shape.Line;
import  javafx.scene.Group;
import  javafx.geometry.Point2D;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Group;


import javafx.scene.control.ScrollPane;

public class Engine {
  private Pane  _pane = null;
  private ArrayList<InteractiveShape> _shapes = new ArrayList<>();
  private Grid _grid = null;
  private Rectangle _board = null;
  private InteractivePolygon _selectedShape = null;
  private Shape _currentMagnetism = null;
  private Point2D _paneOffset = null;

    public Engine(Pane pane, Point2D offset) {
    _pane = pane;
    _paneOffset = offset;
    _board = new Rectangle(0, 0, _pane.getWidth(), _pane.getHeight());
    _board.setStrokeWidth(0.0);
    _board.setFill(Color.TRANSPARENT);

    EventHandler<MouseEvent> mouseClick = event -> {
      System.out.println("PANE");
      if (_selectedShape != null)
        deselect();
    };
    _board.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
    _pane.getChildren().add(_board);
  }

  public void setBackgroundColor(Color color) {
    String val = Integer.toHexString(color.hashCode()).toUpperCase();

    _pane.setStyle("-fx-background-color: #" + val);
  }

  public void activateGrid(boolean val) {
    if (val) {
      if (_grid == null) {
        _grid = new Grid(_pane);
        _grid.setColor(Color.valueOf("#777A81"));
        _grid.setLinesWidth(0.4);
        _grid.setXSpacing(6);
        _grid.setYSpacing(6);
        _grid.activate();
      }
    } else {
      _grid.disable();
      _grid = null;
    }
  }

  public void createInteractivePolygon() {
    InteractivePolygon shape = new InteractivePolygon(this, _pane);
    shape.start();
    _shapes.add(shape);
  }

  public void createInteractiveRectangle() {
    InteractiveRectangle shape = new InteractiveRectangle(this, _pane);
    shape.start();
    _shapes.add(shape);
  }

  public void addInteractiveShape(InteractiveShape intShape) {
    _shapes.add(intShape);
  }

  public void selected(InteractivePolygon selected) {
    if (_selectedShape != null) {
      _selectedShape.deselect();
    }
    _selectedShape = selected;
  }

  public InteractiveShape getSelectedShape() {
    return _selectedShape;
  }

  public void deselect() {
    if (_selectedShape != null) {
      _selectedShape.deselect();
      _selectedShape = null;
    }
  }

  public Pane getPane() {
    return _pane;
  }

  public boolean isObjectUnderCursor(Shape cursor) {
    for (InteractiveShape element : _shapes) {
      if (element == _selectedShape)
        continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          // System.out.println(" ++++++++++ Line collision ++++++++++");
          return true;
        }
      }
      // System.out.println(cursor.getBoundsInParent());
      Shape intersect = Shape.intersect(cursor, element.getShape());
      if (intersect.getBoundsInParent().getWidth() != -1) {
        // System.out.println(intersect.getBoundsInParent().getMaxX() + " : " +
        //   intersect.getBoundsInParent().getMaxX());
        // System.out.println("collision");
        return true;
      }
    }
  return false;
  }

  public Shape getObjectUnderCursor(Shape cursor) {
    if (_currentMagnetism != null
      && Shape.intersect(cursor, _currentMagnetism).getBoundsInParent().getWidth() != -1) {
      return _currentMagnetism;
    }
    for (InteractiveShape element : _shapes) {
      if (element == _selectedShape)
      continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          // System.out.println(" ++++++++++ Stroke ++++++++++");
          _currentMagnetism = shape;
          return shape;
        }
      }
    }
    for (Shape shape : _grid.getLines()) {
      Shape intersect = Shape.intersect(cursor, shape);
      if (intersect.getBoundsInParent().getWidth() != -1) {
        // System.out.println(" ++++++++++ Grid ++++++++++");        
        _currentMagnetism = shape;
        return shape;
      }
    }
    _currentMagnetism = null;
    return null;
  }

  public ScrollPane scrlPane = null;

  public Point2D getCollisionCenter(Shape first, Shape second, Group group) {
    double x = _pane.getScaleX();
    double y = _pane.getScaleY();
    double xLayout = _pane.getParent().getLayoutX();
    double yLayout = _pane.getParent().getLayoutY();

    _pane.setScaleX(1.0);
    _pane.setScaleY(1.0);
    Bounds bounds = scrlPane.getViewportBounds();
    AnchorPane achPane = (AnchorPane) scrlPane.getParent().getParent().getParent().getParent().getParent();
    // TODO: find where this padding come from
    double xPadding = 1;
    double yPadding = 1;
    _pane.setLayoutX(-xLayout - bounds.getMinX() - xPadding);
    _pane.setLayoutY(-yLayout - bounds.getMinY() - achPane.getPadding().getTop() - yPadding);
    // System.out.println(xLayout + " " + bounds.getMinX() + " " + achPane.getPadding().getTop());

    // _pane.setScaleX(1.0);
    // _pane.setScaleY(1.0);
    // // System.out.println("==> " + _paneOffset.getX() + " " + _paneOffset.getY());
    // _pane.setLayoutX(_paneOffset.getX());
    // _pane.setLayoutY(_paneOffset.getY());
    Shape union = Shape.intersect(first, second);
    _pane.setLayoutX(0.0);
    _pane.setLayoutY(0.0);
    _pane.setScaleX(x);
    _pane.setScaleY(y);
    // union.setFill(Color.color(Math.random(), Math.random(), Math.random()));
    // _pane.getChildren().add(union);

    Bounds unionBounds = union.getBoundsInParent();
    Point2D pos = new Point2D(
        (unionBounds.getMinX() + (unionBounds.getMaxX() - unionBounds.getMinX()) / 2),
        (unionBounds.getMinY() + (unionBounds.getMaxY() - unionBounds.getMinY()) / 2));
    // ArrayList<Point2D> test = new ArrayList<>();
    // test.add(new Point2D(unionBounds.getMinX(), unionBounds.getMinY()));
    // test.add(new Point2D(unionBounds.getMaxX(), unionBounds.getMinY()));
    // test.add(new Point2D(unionBounds.getMaxX(), unionBounds.getMaxY()));
    // test.add(new Point2D(unionBounds.getMinX(), unionBounds.getMaxY()));
    // Point2D pos = getCenterOfPoints(test);

    if (group != null)
      pos = group.parentToLocal(pos.getX(), pos.getY());

    return pos;
  }

  public Point2D getCollisionCenter(Shape first, Shape second) {
    return getCollisionCenter(first, second, null);
  }

  public Rectangle getBoard() {
    return _board;
  }

  public Point2D getCenterOfPoints(ArrayList<Point2D> points) {
    double sum1 = 0;
    double sum2 = 0;
    double sum3 = 0;

    for (int i = 0; i < points.size(); ++i) {
      Point2D point1 = points.get(i);
      Point2D point2;
      if (i + 1 == points.size()) {
        point2 = points.get(0);
      } else {
        point2 = points.get(i + 1);
      }
      double val1 = ((point1.getX() * point2.getY()) - (point2.getX() * point1.getY()));
      double val2 = (val1 * (point1.getX() + point2.getX()));
      double val3 = (val1 * (point1.getY() + point2.getY()));
      sum1 += val1;
      sum2 += val2;
      sum3 += val3;
    }

    double air = (sum1 / 2);
    return new Point2D((sum2 / (6 * air)), (sum3 / (6 * air)));
  }
}

