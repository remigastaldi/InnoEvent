/*
 * File Created: Thursday, 22nd November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 23rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;


import javafx.beans.value.ChangeListener;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class CustomCursor {
  Pane _pane = null;

  public enum Type {
    DEFAULT,
    ADD,
    HAND
  };

  private Circle _invisibleBound = null;
  private Circle _visibleShape = null;
  private boolean _manualManagement = false;

  public CustomCursor(Pane pane) {
    // super(startX, startY, radius, Color.TRANSPARENT);
    _pane = pane;

    pane.setOnMouseDragged(event -> {
      if (_invisibleBound != null) {
        _invisibleBound.setCenterX(event.getX());
        _invisibleBound.setCenterY(event.getY());
      }
    });


    pane.setOnMouseMoved(event -> {
      if (_invisibleBound != null) {
        _invisibleBound.setCenterX(event.getX());
        _invisibleBound.setCenterY(event.getY());
      }
    });
  }

  public void setShape(Circle shape, boolean manualManagement) {
    
    if (_visibleShape != null)
      _pane.getChildren().remove(_invisibleBound);
    if (_visibleShape != null && !_manualManagement)
      _pane.getChildren().remove(_visibleShape);
    
    _visibleShape = shape;
    _invisibleBound = new Circle(shape.getCenterX(), shape.getCenterY(), shape.getRadius(), Color.TRANSPARENT);
    _invisibleBound.setStrokeWidth(1.0);
    _invisibleBound.setVisible(false);
    // _invisibleBound.setStroke(Color.RED);
    
    _pane.getChildren().add(_invisibleBound); 
    if (!manualManagement)
      _pane.getChildren().add(_visibleShape);
    _manualManagement = manualManagement;
  }

  public void setShape(Circle shape) { 
    setShape(shape, false);
  }
  
  public void removeShape() {
    _pane.getChildren().remove(_invisibleBound);
    if (!_manualManagement)
      _pane.getChildren().remove(_visibleShape);
    _visibleShape = null;
    _invisibleBound = null;
  }

  public Circle getBoundShape() {
    return _invisibleBound;
  }

  public void enableShape() {
    _visibleShape.setVisible(true);
    _invisibleBound.setVisible(true);
  }

  public void disableShape() {
    _visibleShape.setVisible(false);
    _invisibleBound.setVisible(false);
  }

  public void changeRadius(double radius) {
    _visibleShape.setRadius(radius);
    _invisibleBound.setRadius(radius);
  }

  public void setX(double x) {
    _visibleShape.setCenterX(x);
  }
 
  public void setY(double Y) {
    _visibleShape.setCenterY(Y);
  }

  public double getX() {
    return _visibleShape.getCenterX();
  }
 
  public double getY() {
    return _visibleShape.getCenterY();
  }
  
  public void setForm(CustomCursor.Type type) {
    switch (type) {
      case DEFAULT:
        _pane.setCursor(Cursor.DEFAULT);
        break;
      case ADD:
        setAddForm();
        break;
      case HAND:
        _pane.setCursor(Cursor.HAND);
        break;
    }
  }

  private void setAddForm() {
    double cursorSize = 32;
    double linesWidth = 1.5;

    Circle pt = new Circle(cursorSize / 2, cursorSize / 2, 1, Color.WHITE);

    double ptOffset = cursorSize / 5;
    Line lineXL = new Line(0.0, cursorSize / 2, cursorSize / 2 - ptOffset, cursorSize / 2);
    lineXL.setStroke(Color.WHITE);
    lineXL.setStrokeWidth(linesWidth);
    Line lineXR = new Line(ptOffset + cursorSize / 2, cursorSize / 2, cursorSize, cursorSize / 2);
    lineXR.setStroke(Color.WHITE);
    lineXR.setStrokeWidth(linesWidth);
    Line lineYU = new Line(cursorSize / 2, 0.0, cursorSize / 2, cursorSize / 2 - ptOffset);
    lineYU.setStroke(Color.WHITE);
    lineYU.setStrokeWidth(linesWidth);
    Line lineYD = new Line(cursorSize / 2, ptOffset + cursorSize / 2, cursorSize / 2, cursorSize);
    lineYD.setStroke(Color.WHITE);
    lineYD.setStrokeWidth(linesWidth);

    Node[] nodes = new Node[]{lineXL, lineYU, lineXR, lineYD, pt};
    Group group = new Group(nodes);

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    Image image = group.snapshot(params, null);
    ImageCursor addCursor = new ImageCursor(image, cursorSize / 2, cursorSize / 2);
    _pane.setCursor(addCursor);
  }
}