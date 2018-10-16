/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 16th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.engine.shape;

import  java.util.ArrayList;
import  java.util.HashMap;

import  com.inno.service.engine.Engine;

import  javafx.scene.layout.Pane;
import  javafx.geometry.Point2D;
import  javafx.event.EventHandler;
import  javafx.scene.Cursor;
import  javafx.scene.input.MouseEvent;
import  javafx.scene.shape.Line;
import  javafx.scene.shape.Circle;
import  javafx.scene.shape.Polygon;
import  javafx.scene.paint.Color;
import  javafx.event.EventType;

import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;


public class InteractivePolygon extends InteractiveShape {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  private Polygon _polygon = null;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();

  public InteractivePolygon(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
  }
  
  public void start() {
    
    _cursor = new Circle();
    _cursor.setFill(Color.TRANSPARENT);
    _cursor.setRadius(5.0);
    _cursor.setStroke(Color.GREEN);
    _cursor.setStrokeWidth(2);
    
    _pane.getChildren().add(_cursor);
    
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    // System.out.println(ImageCursor.getBestSize(5, 5));
    WritableImage snapshot = _cursor.snapshot(params, null);
    
    _pane.setCursor(new ImageCursor(snapshot,
    snapshot.getWidth() / 2,
    snapshot.getHeight() /2));
    _cursor.setVisible(false);
    // _pane.setCursor(Cursor.NONE);



    EventHandler<MouseEvent> mouseClickEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseClicked(event))
          addPoint(event);
      }
    };
    _eventHandlers.put(MouseEvent.MOUSE_CLICKED, mouseClickEvent);
    _pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEvent);

    EventHandler<MouseEvent> mouseMovedEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseMoved(event))
          updateCurrentLine(event);
      }
    };
    _eventHandlers.put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    _pane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);

    // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    // _pane.getChildren().add(_polygon);
  }

  private void addPoint(MouseEvent event) {    
    if (_points.size() > 0 && _cursor.intersects(_points.get(0).getBoundsInParent())) {
      _pane.setCursor(Cursor.HAND);
      System.out.println("=============");
      _polygon = new Polygon();

      _polygon.setFill(Color.DODGERBLUE);
      for (Circle point : _points) {
        _polygon.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
      }
      _pane.getChildren().add(_polygon);

      for (Circle point : _points) {
        point.setVisible(false);
      }

      EventHandler<MouseEvent> mouseMovedEvent = _eventHandlers.remove(MouseEvent.MOUSE_MOVED);
      _pane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
      EventHandler<MouseEvent> mouseClickedEvent = _eventHandlers.remove(MouseEvent.MOUSE_CLICKED);
      _pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEvent);

      _pane.setCursor(Cursor.DEFAULT);
      return;
    } 

    Line line = new Line();
    
    line.setStrokeWidth(2.0);
    line.setStroke(Color.KHAKI);
    line.setVisible(false);
    _lines.add(line);
    _pane.getChildren().add(line);
    
    Circle circle = new Circle(event.getX(), event.getY(), 5.0);
    circle.setFill(Color.GREEN);
    
    _points.add(circle);
    _pane.getChildren().addAll(circle);
  }
  
  private void updateCurrentLine(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
    
    if (_points.size() == 0)
      return;
  
    // if (_cursor.intersects(_points.get(0).getBoundsInParent())) {
    //   _pane.setCursor(Cursor.HAND);
    // } else if (_pane.getCursor() == Cursor.HAND)
    //   _pane.setCursor(Cursor.DEFAULT);
    
    Circle lastPoint = _points.get(_points.size() - 1);
    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    activeLine.setStartX(lastPoint.getCenterX());
    activeLine.setStartY(lastPoint.getCenterY());
    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }
}