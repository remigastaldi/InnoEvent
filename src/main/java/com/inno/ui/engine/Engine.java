/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 11th December 2018
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
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class Engine {
  private Pane  _pane = null;
  private ArrayList<InteractiveShape<? extends Shape>> _shapes = new ArrayList<>();
  private Grid _grid = null;
  private Rectangle _board = null;
  private InteractiveShape<? extends Shape> _selectedShape = null;
  private CustomCursor _cursor = null;
  private double _scale = 10.0;
  private MagnetismManager _magenetismManager = null;

  private ScrollPane scrollPane;

  public boolean onBoardSelected(MouseEvent event) { return true; };

  public Engine(StackPane stackPane, double width, double height) {
    _pane = new Pane();
    _magenetismManager = new MagnetismManager(this);

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
      onBoardSelected(event);
      if (_selectedShape != null)
        deselect();
    };
    _board.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
    _pane.getChildren().add(_board);

    _cursor = new CustomCursor(_pane);
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
    } else {
      _grid.destroy();        
      _grid = new Grid(_pane);
      // activateGridMagnetism();
    }
    _grid.setColor(Color.valueOf("#777A81"));
    _grid.setLinesWidth(0.4);
    _grid.setXSpacing(6);
    _grid.setYSpacing(6);
    _grid.activate();
    activateGridMagnetism();
    } else {
      _grid.disable();
      _grid = null;
    }
  }

  public void activateGridMagnetism() {
      for (Shape line : _grid.getLines()) {
        _magenetismManager.registerShape(line);
    }
  }

  public void disableGridMagnetism() {
    for (Shape line : _grid.getLines()) {
      _magenetismManager.removeShape(line);
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

  public InteractiveRectangle createInteractiveRectangle(String id, double x, double y, double width, double height, Rotate rotation, Color color) {
    System.out.println(width + " : " + height);
    InteractiveRectangle section = new InteractiveRectangle(this, getPane(), x, y, width, height, rotation, color);
    addInteractiveShape(section);
    deselect();
    return section;
  }

  public void addInteractiveShape(InteractiveShape<? extends Shape> intShape) {
    _shapes.add(intShape);
    _magenetismManager.registerInteractiveShape(intShape);
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

  public void setBoardWidth(double width) {
    _board.setWidth(width);
    _pane.setPrefWidth(width);
    activateGrid(true);
  }


  public void setBoardHeight(double height) {
    _board.setHeight(height);
    _pane.setPrefHeight(height);
    activateGrid(true);
  }

  // public boolean isObjectUnderCursor(Shape cursor) {
  //   // for (InteractiveShape<? extends Shape> element : _shapes) {
  //   //   if (element == _selectedShape)
  //   //     continue;
  //   //   for (Shape shape : element.getOutBoundShapes()) {
  //   //     Shape intersect = Shape.intersect(cursor, shape);
  //   //     if (intersect.getBoundsInParent().getWidth() != -1) {
  //   //       // System.out.println(" ++++++++++ Line collision ++++++++++");
  //   //       return true;
  //   //     }
  //   //   }
  //   //   // System.out.println(cursor.getBoundsInParent());
  //   //   Shape intersect = Shape.intersect(cursor, element.getShape());
  //   //   if (intersect.getBoundsInParent().getWidth() != -1) {
  //   //     // System.out.println(intersect.getBoundsInParent().getMaxX() + " : " +
  //   //     //   intersect.getBoundsInParent().getMaxX());
  //   //     // System.out.println("collision");
  //   //     return true;
  //   //   }
  //   // }
  // return false;
  // }

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

  public Point2D getCenterOfPoints(double[] points) {
    ArrayList<Point2D> newPoints = new ArrayList<>();

    for (int i = 0; i < points.length; i += 2) {
      newPoints.add(new Point2D(points[i], points[i + 1]));
    }

    return getCenterOfPoints(newPoints);
  }

  public void deleteSelectedShape() {
    if (_selectedShape == null)
      return;
    _shapes.remove(_selectedShape);
    _selectedShape.destroy();
    _selectedShape = null;
  }

  public  double getScale() {
    return _scale;
  }

  public MagnetismManager getMagnetismManager() {
    return _magenetismManager;
  }

  public CustomCursor getCursor() {
    return _cursor;
  }

  public void computeCursorPosition(MouseEvent event, InteractiveShape<? extends Shape> shape) {
    Point2D pos = _magenetismManager.checkMagnetism(_cursor.getBoundShape(), shape);

    if (pos != null) {
      _cursor.setX(pos.getX());
      _cursor.setY(pos.getY());
    } else {
      Point2D groupMouse = null;
      if (shape != null) {
        Point2D mousePos =  _pane.sceneToLocal(event.getSceneX(), event.getSceneY());
         groupMouse = shape.getGroup().parentToLocal(mousePos.getX(), mousePos.getY());
      } else
        groupMouse = new Point2D(event.getX(), event.getY());
      _cursor.setX(groupMouse.getX());
      _cursor.setY(groupMouse.getY());
    }
  }

  public void computeCursorPosition(MouseEvent event) {
    computeCursorPosition(event, null);
  }

  public ArrayList<InteractiveShape<? extends Shape>> getShapes() {
    return _shapes;
  }

  public void toggleMagnetism() {
    _magenetismManager.toggleMagnetism();
  }
}

