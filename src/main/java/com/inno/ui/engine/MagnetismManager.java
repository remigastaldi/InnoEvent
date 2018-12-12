/*
 * File Created: Friday, 23rd November 2018
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

import com.inno.ui.engine.shape.InteractiveShape;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

public class MagnetismManager {
  private ArrayList<InteractiveShape<? extends Shape>> _interactiveShapes = new ArrayList<>();
  private ArrayList<Shape> _shapes = new ArrayList<>();
  private Shape _currentMagnetism = null;
  private Engine _engine = null;
  private boolean _isActive = true;

  public MagnetismManager(Engine engine){
    _engine = engine;
  }

  public void registerShape(Shape shape) {
    _shapes.add(shape);
  }
  
  public void removeShape(Shape shape) {
    _shapes.remove(shape);
  }
  
  public void registerInteractiveShape(InteractiveShape<? extends Shape> shape) {
    _interactiveShapes.add(shape);
  }

  public void removeInteractiveShape(InteractiveShape<? extends Shape> shape) {
    _interactiveShapes.remove(shape);
  }
    
  public Point2D checkMagnetism(Shape shape, InteractiveShape<? extends Shape> interactiveShape) {
    if (!_isActive)
      return null;
    Shape element = getShapeUnder(shape, interactiveShape);
    if (element != null) {
    // System.out.println("Collision " + mousePos.getX());
      Point2D pos = null;
      if (interactiveShape != null) {
        pos = getCollisionCenter(shape, element, interactiveShape.getGroup());
      } else {
        pos = getCollisionCenter(shape, element);
        pos = _engine.getPane().sceneToLocal(pos.getX(), pos.getY());
      }

      // TODO: Collision offset
      return (new Point2D(pos.getX(), pos.getY()));

      // pos = group.localToParent(pos.getX(), pos.getY());
      // Circle circle = new Circle(pos.getX(), pos.getY(), 5, Color.TRANSPARENT);
      // circle.setStroke(Color.ALICEBLUE);
      // circle.setStrokeWidth(1);
      // _engine.getPane().getChildren().add(circle);
    }
    return null;
  }

  public Point2D checkMagnetism(Shape shape) {
    return checkMagnetism(shape, null);
  }

  private Shape getShapeUnder(Shape shape, InteractiveShape<? extends Shape> selected) {
    if (_currentMagnetism != null
        && Shape.intersect(shape, _currentMagnetism).getBoundsInParent().getWidth() != -1) {
      return _currentMagnetism;
    }
    for (InteractiveShape<? extends Shape> element : _interactiveShapes) {
      if (element == selected)
        continue;
      for (Shape outShape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(shape, outShape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          _currentMagnetism = outShape;
          return outShape;
        }
      }  
    }

    for (Shape element : _shapes) {
      if (element == shape)
        continue;
      Shape intersect = Shape.intersect(shape, element);
      if (intersect.getBoundsInParent().getWidth() != -1) {
        _currentMagnetism = element;
        return element;
      }
    }
    _currentMagnetism = null;
    return null;
  }

  public Point2D getCollisionCenter(Shape first, Shape second, Group group) {
    Shape union = Shape.intersect(first, second);
    Bounds unionBounds = union.getBoundsInParent();
    Point2D pos = null;

    if (group != null) {
      pos = group.sceneToLocal((unionBounds.getMinX() + (unionBounds.getMaxX() - unionBounds.getMinX()) / 2),
        (unionBounds.getMinY() + (unionBounds.getMaxY() - unionBounds.getMinY()) / 2));    
    } else {
      pos = new Point2D((unionBounds.getMinX() + (unionBounds.getMaxX() - unionBounds.getMinX()) / 2),
      (unionBounds.getMinY() + (unionBounds.getMaxY() - unionBounds.getMinY()) / 2));
    }

    return pos;
  }

  public Point2D getCollisionCenter(Shape first, Shape second) {
    return getCollisionCenter(first, second, null);
  }

  public void enableMagnetism() {
    _isActive = true;
  }

  public void disableMagnetism() {
    _isActive = false;    
  }

  public void toggleMagnetism() {    
    _isActive = !_isActive;
  }
}