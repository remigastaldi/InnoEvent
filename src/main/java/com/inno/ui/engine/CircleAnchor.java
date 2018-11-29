/*
 * File Created: Wednesday, 21st November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;

import com.inno.ui.engine.shape.InteractiveShape;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class CircleAnchor extends Circle {
  Engine _engine = null;
  InteractiveShape<? extends Shape> _interactiveShape = null;
  DoubleProperty x = null;
  DoubleProperty y = null;

  public CircleAnchor(Engine engine, InteractiveShape< ? extends Shape> intShape, Color color, DoubleProperty x, DoubleProperty y) {
    super(x.get(), y.get(), 5);
    init(engine, intShape, color, x, y, false);
  }

  public CircleAnchor(Engine engine, InteractiveShape< ? extends Shape> intShape, Color color, DoubleProperty x, DoubleProperty y, boolean bidirectional) {
    super(x.get(), y.get(), 5);
    init(engine, intShape, color, x, y, bidirectional);
  }

  private void init(Engine engine, InteractiveShape< ? extends Shape> intShape, Color color, DoubleProperty x, DoubleProperty y, boolean bidirectional) {
    _engine = engine;
    _interactiveShape = intShape;

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
    
    enableDrag();    
  }

  private boolean _mousePressed = false;
  private void enableDrag() {
    setOnMouseEntered(mouseEvent -> {
      _engine.getCursor().setForm(CustomCursor.Type.HAND);
    });

    setOnMouseExited(mouseEvent -> {
      if (!_mousePressed) {
        _engine.getCursor().setForm(CustomCursor.Type.DEFAULT);
      }
    });

    setOnMousePressed(mouseEvent -> {
      _mousePressed = true;
      _engine.getCursor().setShape(this, true);
    });

    setOnMouseReleased(mouseEvent -> { 
      _mousePressed = false;
      _engine.getCursor().setForm(CustomCursor.Type.DEFAULT);
      _engine.getCursor().removeShape();
      // _interactiveShape.onShapeChanged();
    });

    setOnMouseDragged(mouseEvent -> {
      _engine.computeCursorPosition(mouseEvent, _interactiveShape);
      setCenterX(_engine.getCursor().getX());
      setCenterY(_engine.getCursor().getY());

      // if (_engine.isObjectUnderCursor(getShape())) {
      //   _rectangle.setFill(Color.RED);
      // } else {
      //   _rectangle.setFill(Color.GREEN);
      // }
      _interactiveShape.onShapeResized();
    });
  }

  public Shape getShape() {
    return (Circle) this;
  }
}