/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import java.util.ArrayList;
import java.util.HashMap;

import com.inno.ui.engine.CustomCursor;
import com.inno.ui.engine.Engine;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class InteractiveShape<T extends Shape> {
  private Engine  _engine = null;
  private Pane _pane = null;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();
  private ArrayList<Shape> _outBoundShapes = new ArrayList<>();
  private ArrayList<Shape> _selectShapes = new ArrayList<>();
  private String _id = null;
  // TODO: pass to private
  protected double _rotation = 0.0;
  protected T _shape = null;
  protected Group _group;

  InteractiveShape(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
  }

  InteractiveShape(Engine engine, Pane pane, double rotation) {
    _engine = engine;
    _pane = pane;
    _rotation = rotation;
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
  public boolean onDestroy() { return true; }
  public void onShapeChanged() {};

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

  public void setID(String id) {
    _id = id;
  }

  public String getID() {
    return _id;
  }

  public void setRotation(double rotation) {
    _rotation = rotation;

  }

  public Double getRotation() {
    return _rotation;
  }
}