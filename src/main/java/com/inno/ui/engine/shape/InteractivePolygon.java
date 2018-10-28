/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
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
import  javafx.event.EventHandler;
import  javafx.event.EventType;

import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;


public class InteractivePolygon extends InteractiveShape {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  private Polygon _polygon = null;
  private Circle _cursor = null;

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
    // System.out.println(ImageCursor.getBestSize(5, 5));
    WritableImage snapshot = _cursor.snapshot(params, null);
    Pane().setCursor(new ImageCursor(snapshot,
    snapshot.getWidth() / 2,
    snapshot.getHeight() /2));

    _cursor.setVisible(false);
    // Pane().setCursor(Cursor.NONE);

    EventHandler<MouseEvent> mouseClickEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseClicked(event)) {
          if (_points.size() > 0 && _cursor.intersects(_points.get(0).getBoundsInParent())) {
            closeForm();
          } else {
            addPoint(event);
          }
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseClickEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseClickEvent);

    EventHandler<MouseEvent> mouseMovedEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseMoved(event))
          updateCurrentLine(event);
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
      
    return;
  }
  
  private void updateCurrentLine(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
    
    if (_points.size() == 0)
      return;
  
    // if (_cursor.intersects(_points.get(0).getBoundsInParent())) {
    //   Pane().setCursor(Cursor.HAND);
    // } else if (Pane().get_cursor == Cursor.HAND)
    //   Pane().setCursor(Cursor.DEFAULT);
    
    Circle lastPoint = _points.get(_points.size() - 1);
    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    activeLine.setStartX(lastPoint.getCenterX());
    activeLine.setStartY(lastPoint.getCenterY());
    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }

  private void select() {
    System.out.println("SHAPE" + this);

    for (Circle point : _points) {
      point.setVisible(true);
      point.toFront();
    }
    Engine().selected(this);
  }

  public void deselect() {
    for (Circle point : _points) {
      point.setVisible(false);
    }
  }
}