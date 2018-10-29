/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 28th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine.shape;

import  java.util.ArrayList;
import  java.util.HashMap;

import  com.inno.ui.engine.Engine;

import  javafx.scene.layout.Pane;
import  javafx.geometry.Point2D;
import  javafx.scene.Cursor;
import  javafx.scene.input.MouseEvent;
import  javafx.scene.shape.Line;
import  javafx.scene.shape.Circle;
import  javafx.scene.shape.Polygon;
import  javafx.scene.paint.Color;
import  javafx.collections.ObservableList;
import  javafx.event.EventHandler;
import  javafx.event.EventType;

import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.collections.FXCollections;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.*;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
// import java.lang.Number;

// import javafx.scene.anchors.Anchor;

public class InteractivePolygon extends InteractiveShape {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  private Polygon _polygon = null;
  private Circle _cursor = null;
  private ObservableList<Anchor> _anchors = null;
  private boolean _collisionDetected = false;
  InteractivePolygon _this = this;

  public InteractivePolygon(Engine engine, Pane pane) {
    super(engine, pane);
  }

  public void start() {
    // Bound check cursor
    _cursor = new Circle();
    _cursor.setFill(Color.TRANSPARENT);
    _cursor.setRadius(5.0);
    _cursor.setStroke(Color.GREEN);
    _cursor.setStrokeWidth(2);
    Pane().getChildren().add(_cursor);
    
    // System cursor
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    Image addIcon = new Image("icon/add.png");
    Image closeIcon = new Image("icon/close.png");
    ImageCursor addCursor = new ImageCursor(addIcon,
                                            addIcon.getWidth() / 2,
                                            addIcon.getHeight() /2);
    ImageCursor closeCursor = new ImageCursor(closeIcon,
                                            closeIcon.getWidth() / 2,
                                            closeIcon.getHeight() /2);
    Pane().setCursor(addCursor);

    _cursor.setVisible(false);
    // Pane().setCursor(Cursor.NONE);

    EventHandler<MouseEvent> mouseClickEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseClicked(event)) {
          if (_points.size() > 0 && _cursor.intersects(_points.get(0).getBoundsInParent())) {
            closeForm();
          } else {
            if (!_collisionDetected)
              addPoint(event);
          }
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseClickEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseClickEvent);

    EventHandler<MouseEvent> mouseMovedEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseMoved(event)) {
          _collisionDetected = Engine().isObjectUnderCursor(_cursor) ||
            (_lines.size() > 0 ? Engine().isObjectUnderCursor(_lines.get(_lines.size() - 1)) : false);
          if (_collisionDetected) {
            if (_lines.size() > 0)
            _lines.get(_lines.size() - 1).setStroke(Color.RED);
            Pane().setCursor(closeCursor);
            
          } else {
            if (_lines.size() > 0)
            _lines.get(_lines.size() - 1).setStroke(Color.KHAKI);
            Pane().setCursor(addCursor);
          }
        updateCursor(event);
        updateCurrentLine(event);
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);

    // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    // Pane().getChildren().add(_polygon);
  }

  private void addPoint(MouseEvent event) {    
    Line line = new Line();
    
    line.setStrokeWidth(2.0);
    line.setStroke(Color.KHAKI);
    line.setVisible(false);
    line.setStartX(event.getX());
    line.setStartY(event.getY());
    _lines.add(line);
    Pane().getChildren().add(line);
    
    Circle circle = new Circle(event.getX(), event.getY(), 5.0);
    circle.setFill(Color.GREEN);
    
    _points.add(circle);
    Pane().getChildren().addAll(circle);
  }

  private void closeForm() {
    // Pane().setCursor(Cursor.HAND);
    System.out.println("close form");
    _polygon = new Polygon();

    _polygon.setFill(Color.DODGERBLUE);
    for (Circle point : _points) {
      _polygon.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
    }
    Pane().getChildren().add(_polygon);
    // createControlAnchorsFor(_polygon.getPoints());
    _anchors = createControlAnchorsFor(_polygon.getPoints());
    // Pane().getChildren().addAll(_anchors);

    for (Circle point : _points) {
      point.setVisible(false);
    }

    Line activeLine = _lines.get(_lines.size() - 1);
    Circle firstPoint = _points.get(0);
    activeLine.setEndX(firstPoint.getCenterX());
    activeLine.setEndY(firstPoint.getCenterY());

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    // EventHandler<MouseEvent> mouseClickedEvent = EventHandlers().remove(MouseEvent.MOUSE_CLICKED);
    // Pane().removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEvent);
    EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
    Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);

    Pane().setCursor(Cursor.DEFAULT);

    // Add form selection callback
    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        select();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_CLICKED, mouseClick);
    _polygon.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
    
    Engine().addInteractiveShape(this);
    select();
    return;
  }
  
  private void updateCursor(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
  }

  private void updateCurrentLine(MouseEvent event) {
    if (_points.size() == 0)
      return;
  
    // if (_cursor.intersects(_points.get(0).getBoundsInParent())) {
    //   Pane().setCursor(Cursor.HAND);
    // } else if (Pane().get_cursor == Cursor.HAND)
    //   Pane().setCursor(Cursor.DEFAULT);
    
    // Circle lastPoint = _points.get(_points.size() - 1);
    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    // activeLine.setStartX(lastPoint.getCenterX());
    // activeLine.setStartY(lastPoint.getCenterY());
    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }

  private void select() {
    System.out.println("SHAPE" + this);

    if (Engine().getSelectedShape() != this) {
      Pane().getChildren().addAll(_anchors);
      Engine().selected(this);
    }
  }

  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    int j = 0;
    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;
      final int idj = j;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
          _lines.get(idj - 1 < 0 ? _lines.size() -1 : idj - 1).setEndX((double) x);
          _lines.get(idj).setStartX((double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
          _lines.get(idj - 1 < 0 ? _lines.size() -1 : idj - 1).setEndY((double) y);
          _lines.get(idj).setStartY((double) y);
        }
      });
      j++;
      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  class Anchor extends Circle {
    private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 5);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    public DoubleProperty getX() {
      return this.x;
    }

    public DoubleProperty getY() {
      return this.y;
    }

    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          // getScene().setCursor(Cursor.MOVE);
        }
      });
      // setOnMouseReleased(new EventHandler<MouseEvent>() {
      //   @Override public void handle(MouseEvent mouseEvent) {
      //     getScene().setCursor(Cursor.HAND);
      //   }
      // });
      Anchor anchor = this;
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          double newY = mouseEvent.getY() + dragDelta.y;
          updateCursor(mouseEvent);
          // _this._collisionDetected = _this.Engine().isObjectUnderCursor(_this.getCursor());
          Circle tmp = new Circle(newX, newY, 5, Color.TRANSPARENT);
          Pane().getChildren().add(tmp);
          _collisionDetected = _this.Engine().isObjectUnderCursor(tmp);
          // _this._collisionDetected = _this.Engine().isObjectUnderCursor(_this.getShape());
          if (_collisionDetected) {
            setStroke(Color.RED);
          } else {
            setStroke(Color.GOLD);
            if (newX > 0 && newX < getScene().getWidth()) {
              setCenterX(newX);
            }
            // double newY = mouseEvent.getY() + dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight()) {
              setCenterY(newY);
            }
          }
          if (_this.Engine().isObjectUnderCursor(_this.getShape())) {
            _polygon.setFill(Color.RED);
          } else {
            _polygon.setFill(Color.GREEN);
          }
        }
      });
    }
  }

  public void deselect() {
    // for (Circle point : _points) {
    //   point.setVisible(false);
    // }
    Pane().getChildren().removeAll(_anchors);
  }
  private class Delta { double x, y; }

  public Shape getShape() {
    return _polygon;
  }

  public Circle getCursor() {
    return _cursor;
  }
}