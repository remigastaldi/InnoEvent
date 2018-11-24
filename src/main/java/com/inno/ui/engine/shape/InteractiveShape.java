/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 23rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import  javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;

import com.inno.ui.engine.CustomCursor;
import  com.inno.ui.engine.Engine;
import  javafx.scene.layout.Pane;
import  javafx.scene.shape.Circle;
import  javafx.event.EventType;
import  javafx.event.EventHandler;
import  javafx.scene.paint.Color;
import  javafx.event.EventHandler;
import  javafx.scene.shape.Shape;
import javafx.scene.Group;
import javafx.scene.Node;
import  javafx.scene.effect.DropShadow;

public abstract class InteractiveShape<T extends Shape> {
  private Engine  _engine = null;
  private Pane  _pane = null;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();
  private ArrayList<Shape> _outBoundShapes = new ArrayList<>();
  private ArrayList<Shape> _selectShapes = new ArrayList<>();
  // TODO: pass to private
  protected T _shape = null;
  protected Group _group;

  InteractiveShape(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
  }

  // Callback
  public boolean onMouseEntered(MouseEvent event) { return true; }
  public boolean onMouseClicked(MouseEvent event) { return true; }
  public boolean onMouseExited(MouseEvent event)  { return true; }
  public boolean onMouseMoved(MouseEvent event)   { return true; }
  public boolean onMousePressed(MouseEvent event)  { return true; }
  public boolean onMouseReleased(MouseEvent event) { return true; }
  public boolean onMouseOnDragDetected(MouseEvent event) { return true; }
  public boolean onMouseOnDragDropped(MouseEvent event) { return true; }
  public boolean onFormComplete() { return true; }

  public abstract void start();
  public abstract void deselect();
  public abstract void destroy();
  public abstract Shape getShape();

  protected void addOutboundShape(Shape shape) {
    _outBoundShapes.add(shape);
  }

  public ArrayList<Shape> getOutBoundShapes() {
    return _outBoundShapes;
  }

  public ArrayList<Shape> getSelectShapes() {
    return _selectShapes;
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

  public CustomCursor Cursor() {
    return _engine.getCursor();
  }

  public void enableGlow() {
    int depth = 10;
    DropShadow borderGlow = new DropShadow();
    borderGlow.setOffsetY(0f);
    borderGlow.setOffsetX(0f);
    borderGlow.setColor(Color.GOLD);
    borderGlow.setWidth(depth);
    borderGlow.setHeight(depth);

    _shape.setEffect(borderGlow);
  }

  public void disableGlow() {
    _shape.setEffect(null);
  }

  public Group getGroup() {
    return _group;
  }
}