/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 31st October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import  javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;

import  com.inno.ui.engine.Engine;
import  javafx.scene.layout.Pane;
import  javafx.scene.shape.Circle;
import  javafx.event.EventType;
import  javafx.event.EventHandler;
import  javafx.scene.paint.Color;
import  javafx.event.EventHandler;
import  javafx.scene.shape.Shape;

public abstract class InteractiveShape {
  private Engine  _engine = null;
  private Pane  _pane = null;
  private boolean _collision = false;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();
  private ArrayList<Shape> _outBoundShapes = new ArrayList<>();

  protected ArrayList<Shape> _extShapes = new ArrayList<>();

  InteractiveShape(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
    
  }

  public abstract void start();
  
  public void enableCollision(boolean collision) { _collision = collision; 
  }

  public boolean onMouseEntered(MouseEvent event) { return true; }
  public boolean onMouseClicked(MouseEvent event) { return true; }
  public boolean onMouseExited(MouseEvent event)  { return true; }
  public boolean onMouseMoved(MouseEvent event)   { return true; }
  public boolean onMousePressed(MouseEvent event)  { return true; }
  public boolean onMouseReleased(MouseEvent event) { return true; }
  public boolean onMouseOnDragDetected(MouseEvent event) { return true; }
  public boolean onMouseOnDragDropped(MouseEvent event) { return true; }
  public abstract void deselect();
  public abstract Shape getShape();

  protected void addOutboundShape(Shape shape) {
    _outBoundShapes.add(shape);
  }

  public ArrayList<Shape> getOutBoundShapes() {
    return _outBoundShapes;
  }

  //TODO remove this
  public ArrayList<Shape> getExtShapes() {
    return _extShapes;
  }

  protected Pane Pane() {
    return _pane;
  }

  protected HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> EventHandlers() {
    return _eventHandlers;
  }

  protected Engine Engine() {
    return _engine;
  }
}