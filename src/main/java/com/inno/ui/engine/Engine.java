/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 22nd November 2018
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

import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.StackPane;


import javafx.scene.control.ScrollPane;

public class Engine {
  private Pane  _pane = null;
  private ArrayList<InteractiveShape<? extends Shape>> _shapes = new ArrayList<>();
  private Grid _grid = null;
  private Rectangle _board = null;
  private InteractiveShape<? extends Shape> _selectedShape = null;
  private Shape _currentMagnetism = null;
  private double _scale = 10.0;

  private ScrollPane scrollPane;


  public Engine(StackPane stackPane, double width, double height) {
    // Pane _pane = new Pane();
    _pane = new Pane();

    _pane.setPrefSize(width, height);

    Group group = new Group(_pane);
    StackPane content = new StackPane(group);
    content.setStyle("-fx-background-color: #1E1E1E");

    group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
      // keep it at least as large as the content
      content.setMinWidth(newBounds.getWidth());
      content.setMinHeight(newBounds.getHeight());
    });

    scrollPane = new ScrollPane(content);
    scrollPane.setStyle("-fx-background-color: #1E1E1E");

    // scrollPane.setPannable(true);
    scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
        // use vieport size, if not too small for zoomTarget
        content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
    });

    content.setOnScroll(evt -> {
      if (evt.isControlDown()) {
          evt.consume();

          final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;

          Point2D scrollOffset = figureScrollOffset(group, scrollPane);

          // do the resizing
          _pane.setScaleX(zoomFactor * _pane.getScaleX());
          _pane.setScaleY(zoomFactor * _pane.getScaleY());

          // refresh ScrollPane scroll positions & content bounds
          scrollPane.layout();

          repositionScroller(group, scrollPane, zoomFactor, scrollOffset);
      }
    });
    stackPane.getChildren().add(scrollPane);

    _board = new Rectangle(0, 0, _pane.getWidth(), _pane.getHeight());
    _board.setStrokeWidth(0.0);
    _board.setFill(Color.TRANSPARENT);

    _pane.prefWidthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      _board.setWidth(newX.doubleValue());
    });

    _pane.prefHeightProperty().addListener((ChangeListener<Number>) (ov, oldX, newY) -> {
      _board.setHeight(newY.doubleValue());
    });

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

  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();

    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2;
      double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }

    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
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

  public void addInteractiveShape(InteractiveShape<? extends Shape> intShape) {
    _shapes.add(intShape);
  }

  public void selected(InteractiveShape<? extends Shape> selected) {
    if (_selectedShape != null) {
      _selectedShape.deselect();
    }
    _selectedShape =  selected;
  }

  public InteractiveShape<? extends Shape> getSelectedShape() {
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
    for (InteractiveShape<? extends Shape> element : _shapes) {
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

    for (InteractiveShape<? extends Shape> element : _shapes) {
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
    for (InteractiveShape<? extends Shape> element : _shapes) {
      if (element == _selectedShape)
        continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
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
    _shapes.remove(_selectedShape);
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

