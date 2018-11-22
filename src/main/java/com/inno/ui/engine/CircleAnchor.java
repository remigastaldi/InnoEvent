/*
 * File Created: Wednesday, 21st November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 22nd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

public class CircleAnchor extends Circle {
  Engine _engine = null;
  Circle _cursor = null;
  Group _group  = null;
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

    _cursor = new Circle(x.get(), y.get(), 5.0, Color.TRANSPARENT);
    _engine.getPane().getChildren().add(_cursor);
    enableDrag();

  }

  public void setGroup(Group group) {
    _group = group;
  }

  // TODO Change this function logic place to magnetic class ???
  private void enableDrag() {
    setOnMouseDragged(mouseEvent -> {
      Point2D mousePos =  _engine.getPane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      _cursor.setCenterX(mousePos.getX());
      _cursor.setCenterY(mousePos.getY());

      Shape element = _engine.getObjectUnderCursor(_cursor);

      if (element != null) {
        // System.out.println("Collision " + mousePos.getX());
        Point2D pos = null;
        if (_group != null) {
          pos = _engine.getCollisionCenter(_cursor, element, _group);
        } else {
          pos = _engine.getCollisionCenter(_cursor, element);
          pos = _engine.getPane().sceneToLocal(pos.getX(), pos.getY());
        }

        // TODO: Collision offset

        setCenterX(pos.getX());
        setCenterY(pos.getY());

        // pos = _group.localToParent(pos.getX(), pos.getY());
        // Circle circle = new Circle(pos.getX(), pos.getY(), 5, Color.TRANSPARENT);
        // circle.setStroke(Color.ALICEBLUE);
        // circle.setStrokeWidth(1);
        // _engine.getPane().getChildren().add(circle);
      } else {
        Point2D groupMouse = null;
        if (_group != null) {
           groupMouse = _group.parentToLocal(mousePos.getX(), mousePos.getY());
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