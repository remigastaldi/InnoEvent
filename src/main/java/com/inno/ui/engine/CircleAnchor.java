/*
 * File Created: Wednesday, 21st November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 23rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import com.inno.ui.engine.shape.InteractiveShape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

public class CircleAnchor extends Circle {
  Engine _engine = null;
  // Circle _cursor = null;
  InteractiveShape<? extends Shape> _interactiveShape  = null;
  DoubleProperty x = null;
  DoubleProperty y = null;

  public CircleAnchor(Engine engine, Color color, DoubleProperty x, DoubleProperty y) {
    super(x.get(), y.get(), 5);
    init(engine, color, x, y, false);
  }

  public CircleAnchor(Engine engine, Color color, DoubleProperty x, DoubleProperty y, boolean bidirectional) {
    super(x.get(), y.get(), 5);
    init(engine, color, x, y, bidirectional);
  }

  private void init(Engine engine, Color color, DoubleProperty x, DoubleProperty y, boolean bidirectional) {
    _engine = engine;

    setFill(color.deriveColor(1, 1, 1, 0.5));
    setStroke(color);
    setStrokeWidth(1);
    
    this.x = x;
    this.y = y;
    
    if (bidirectional) {
      x.bindBidirectional(centerXProperty());
      y.bindBidirectional(centerYProperty());
    } else {
      x.bind(centerXProperty());
      y.bind(centerYProperty());
    }
    
    // _cursor = new Circle(x.get(), y.get(), 5.0, Color.TRANSPARENT);
    // _engine.getPane().getChildren().add(_cursor);
    enableDrag();
    
  }
  
  public void setInteractiveShape(InteractiveShape<? extends Shape> interactiveShap) {
    _interactiveShape = interactiveShap;
  }

  private boolean _mousePressed = false;
  private void enableDrag() {
    setOnMouseEntered(mouseEvent -> {
      _engine.getCursor().setForm(CustomCursor.Type.HAND);
    });

    setOnMouseExited(mouseEvent -> {
      _engine.getCursor().setForm(CustomCursor.Type.DEFAULT);
    });

    setOnMousePressed(mouseEvent -> {
      _mousePressed = true;
      _engine.getCursor().setShape(this, true);
    });

    setOnMouseReleased(mouseEvent -> { 
      _mousePressed = false;
      _engine.getCursor().setForm(CustomCursor.Type.DEFAULT);
      _engine.getCursor().removeShape();
    });

    setOnMouseDragged(mouseEvent -> {
      Point2D pos = _engine.getMagnetismManager().checkMagnetism(_engine.getCursor().getBoundShape(), _interactiveShape);

      if (pos != null) {
        setCenterX(pos.getX());
        setCenterY(pos.getY());
      } else {
        Point2D groupMouse = null;
        if (_interactiveShape != null) {
          Point2D mousePos =  _engine.getPane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
           groupMouse = _interactiveShape.getGroup().parentToLocal(mousePos.getX(), mousePos.getY());
        } else
          groupMouse = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        setCenterX(groupMouse.getX());
        setCenterY(groupMouse.getY());
      }

      // if (_engine.isObjectUnderCursor(getShape())) {
      //   _rectangle.setFill(Color.RED);
      // } else {
      //   _rectangle.setFill(Color.GREEN);
      // }
    });
  }

  public Shape getShape() {
    return (Circle) this;
  }
}