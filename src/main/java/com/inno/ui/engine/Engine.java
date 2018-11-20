/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 19th November 2018
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
  private InteractiveShape _selectedShape = null;
  private Shape _currentMagnetism = null;
  private double _scale = 10.0;

    public Engine(Pane pane) {
    _pane = pane;
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

  public void selected(InteractiveShape selected) {
    if (_selectedShape != null) {
      _selectedShape.deselect();
    }
    _selectedShape =  selected;
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

  public ArrayList<Shape> getObjectsUnderCursor(Shape cursor) {
    ArrayList<Shape> shapes = new ArrayList<>();

    for (InteractiveShape element : _shapes) {
      if (element == _selectedShape)
        continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          shapes.add(shape);
        }
      }
    }
    return shapes;
  }

  public Shape getObjectUnderCursor(Shape cursor) {
    // TODO: Change this ligique with magnetism class
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
    Shape gridShape = _grid.checkGridIntersect(cursor);
    if (gridShape != null) {
      _currentMagnetism = gridShape;
      return gridShape;
    }
    _currentMagnetism = null;
    return null;
  }

  public Point2D getCollisionCenter(Shape first, Shape second, Group group) {
    Shape union = Shape.intersect(first, second);
    Bounds unionBounds = union.getBoundsInParent();
    Point2D pos = null;

    if (group != null) {
      pos = group.sceneToLocal((unionBounds.getMinX() + (unionBounds.getMaxX() - unionBounds.getMinX()) / 2),
        (unionBounds.getMinY() + (unionBounds.getMaxY() - unionBounds.getMinY()) / 2));    
    } else {
      pos = new Point2D((unionBounds.getMinX() + (unionBounds.getMaxX() - unionBounds.getMinX()) / 2),
      (unionBounds.getMinY() + (unionBounds.getMaxY() - unionBounds.getMinY()) / 2));
    }

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

  public void deleteSelectedShape() {
    if (_selectedShape == null)
      return;
    _selectedShape.destroy();
    _selectedShape = null;
  }

  public double pixelToMeter(double pixel) {
    return pixel / _scale;
  }

  public double meterToPixel(double meter) {
    return meter * _scale;
  }
}

