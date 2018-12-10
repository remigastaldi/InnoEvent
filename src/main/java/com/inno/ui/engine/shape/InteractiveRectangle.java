/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 9th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import com.inno.ui.engine.CircleAnchor;
import com.inno.ui.engine.CustomCursor;
import com.inno.ui.engine.Engine;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class InteractiveRectangle extends InteractiveShape<Rectangle> {
  private boolean _collisionDetected = false;

  private DoubleProperty maxXProperty = null;
  private DoubleProperty maxYProperty = null;

  private Rotate _rotation;

  public InteractiveRectangle(Engine engine, Pane pane) {
    super(engine, pane);

  }

  public InteractiveRectangle(Engine engine, Pane pane, double x, double y, double width, double height, Rotate rotation, Color color) {
    super(engine, pane);

    closeForm(x, y, width, height, rotation, color);
  }

  public void start() {
    java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
    Point2D local = Pane().screenToLocal(mouse.x, mouse.y);

    Circle cursorForm = new Circle(local.getX(), local.getY(), 5.0, Color.TRANSPARENT);
    cursorForm.setStroke(Color.GOLD);
    cursorForm.setStrokeWidth(1.0);
    // cursorForm.setVisible(false);

    Cursor().setShape(cursorForm);
    Cursor().setForm(CustomCursor.Type.ADD);

    EventHandler<MouseEvent> mouseMovedEvent = event -> {
      if (onMouseMoved(event)) {
        Engine().computeCursorPosition(event);
      }
    };
    
    EventHandler<MouseEvent> mouseDraggedEvent = event -> {
      if (onMouseOnDragDetected(event)) {
        Engine().computeCursorPosition(event, this);
        maxXProperty.set(Cursor().getX());
        maxYProperty.set(Cursor().getY());
      }
    };
    EventHandler<MouseEvent> mouseReleasedEvent = event -> {
      if (onMouseReleased(event)) {
        if (_collisionDetected)
          return;
        EventHandler<MouseEvent> mouseReleaseEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
        Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleaseEvent);
        Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEvent);
        Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
        Cursor().removeShape();
        Cursor().setForm(CustomCursor.Type.DEFAULT);
        // if (!onFormComplete())
        //   return;
      }
    };
    EventHandler<MouseEvent> mousePressedEvent = event -> {
      if (onMousePressed(event)) {
        if (_collisionDetected)
          return;
        closeForm(event.getX(), event.getY());
        Cursor().setForm(CustomCursor.Type.HAND);
      }
    };

    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDraggedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEvent);
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);
    EventHandlers().put(MouseEvent.MOUSE_PRESSED, mousePressedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedEvent);
  }

  private ObservableList<CircleAnchor> createRectangleAnchors() {
    ObservableList<CircleAnchor> anchors = FXCollections.observableArrayList();

    maxXProperty = new SimpleDoubleProperty();
    maxYProperty = new SimpleDoubleProperty();

    maxXProperty.set(_shape.getX() + _shape.getWidth());
    maxYProperty.set(_shape.getY() + _shape.getHeight());

    CircleAnchor resizeHandleLU = new CircleAnchor(Engine(), this, Color.GOLD, _shape.xProperty(), _shape.yProperty(), true);
    CircleAnchor resizeHandleRU = new CircleAnchor(Engine(), this, Color.GOLD, maxXProperty, _shape.yProperty(), true);
    CircleAnchor resizeHandleRD = new CircleAnchor(Engine(), this, Color.GOLD, maxXProperty, maxYProperty, true);
    CircleAnchor resizeHandleLD = new CircleAnchor(Engine(), this, Color.GOLD, _shape.xProperty(), maxYProperty, true);

    _shape.widthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (_shape.getX() + newX.doubleValue() != maxXProperty.get()) {
        maxXProperty.set(_shape.getX() + newX.doubleValue());
        // onShapeResized();
      }
    });
    
    _shape.heightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (_shape.getY() + newY.doubleValue() != maxYProperty.get()) {
        maxYProperty.set(_shape.getY() + newY.doubleValue());
        // onShapeResized();
      }
    });

    resizeHandleLU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (maxXProperty.get() - _shape.getX() != _shape.getWidth()) {
        // Point2D pos = _group.localToParent(new Point2D(newX.doubleValue(), getY()));
        // System.out.println("++++++++++++++++++++++++++++++++++ X " + pos.getX() + " Y " + pos.getY());
        // System.out.println("++++++++++++++++++++++++++++++++++= X " + newX.doubleValue() + " Y " + getY());
        // _group.relocate(newX .doubleValue(), getY());
        // _shape.setX(newX.doubleValue());
        _shape.setWidth(maxXProperty.get() - _shape.getX());
        // onShapeResized();
      }
    });
    resizeHandleLU.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (maxYProperty.get() - _shape.getY() != _shape.getHeight()) {
        // System.out.println("++++++++++++++++++++++++++++++++++= X" + getX() + " Y " + newY.doubleValue());
      // Point2D pos = _group.localToParent(new Point2D(getX(), newY.doubleValue()));
      // _group.relocate(getX(), newY.doubleValue());
      // _shape.setY(newY.doubleValue());
      _shape.setHeight(maxYProperty.get() - _shape.getY());
      // onShapeResized();
      }
    });
    
    resizeHandleRU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (maxXProperty.get() - _shape.getX() != _shape.getWidth()) {
        _shape.setWidth(maxXProperty.get() - _shape.getX());
      }
      // onShapeResized();
    });

    resizeHandleRD.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (maxYProperty.get() - _shape.getY() != _shape.getHeight()) {
        _shape.setHeight(maxYProperty.get() - _shape.getY());
      // onShapeResized();
      }
    });

    anchors.add(resizeHandleLU);
    anchors.add(resizeHandleRU);
    anchors.add(resizeHandleRD);
    anchors.add(resizeHandleLD);

    addSelectShape(resizeHandleLU);
    addSelectShape(resizeHandleRU);
    addSelectShape(resizeHandleRD);
    addSelectShape(resizeHandleLD);


    createLine(resizeHandleLU, resizeHandleRU);
    createLine(resizeHandleRU, resizeHandleRD);
    createLine(resizeHandleRD, resizeHandleLD);
    createLine(resizeHandleLD, resizeHandleLU);

    return anchors;
  }

  void createLine(CircleAnchor first, CircleAnchor second) {
    Line line = new Line();
    line.setStrokeWidth(1.0);
    line.setStroke(Color.ROYALBLUE);
    line.startXProperty().bind(first.centerXProperty());
    line.startYProperty().bind(first.centerYProperty());
    line.endXProperty().bind(second.centerXProperty());
    line.endYProperty().bind(second.centerYProperty());

    addOutboundShape(line);
  }

  public void setPoints(double[] pos) {
    setX(pos[0]);
    setY(pos[1]);
    setWidth(Math.hypot(pos[0] - pos[2], pos[1] - pos[3]));
    setHeight(Math.hypot(pos[6] - pos[0], pos[7] - pos[1]));
  }

  public void setX(double x) {
    _shape.setX(x);
  }

  public void setY(double y) {
    _shape.setY(y);
  }

  public double getX() {
    return _shape.getX();
  }

  public double getY() {
    return _shape.getY();
  }

  public void setSize(double width, double height) {
    _shape.setWidth(width);
    _shape.setHeight(height);
  }

  public void setWidth(double width) {
    _shape.setWidth(width);
  }

  public void setHeight(double height) {
    _shape.setHeight(height);
  }

  public double getWidth() {
    return _shape.getWidth();
  }

  public double getHeight() {
    return _shape.getHeight();
  }

  public DoubleProperty xProperty() {
    return _shape.xProperty();
  }

  public DoubleProperty yProperty() {
    return _shape.yProperty();
  }

  public DoubleProperty maxXProperty() {
    return maxXProperty;
  }

  public DoubleProperty maxYProperty() {
    return maxYProperty;
  }

  public DoubleProperty widthProperty() {
    return _shape.widthProperty();
  }

  public DoubleProperty heightProperty() {
    return _shape.heightProperty();
  }

  public void closeForm(double x, double y, double width, double height, Rotate rotation, Color color) {
    setShape(new Rectangle(x, y, width, height));
    System.out.println("################### CREATED ######################");


    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    if (mouseMovedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mousePressedEvent = EventHandlers().remove(MouseEvent.MOUSE_PRESSED);
    if (mousePressedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedEvent);

    _anchors = createRectangleAnchors();

    Cursor().setForm(CustomCursor.Type.DEFAULT);

    setColor(color);
    setRotation(rotation);

    Engine().getMagnetismManager().registerInteractiveShape(this);
    
    enableSelection();
    select();
    
    onFormComplete();
    return;
  }

  public void closeForm(double x, double y) {
    closeForm(x, y, 1, 1, new Rotate(0,0,0), Color.ROYALBLUE);
  }

  public void closeForm(double x, double y, Color color) {
    closeForm(x, y, 1, 1, new Rotate(0,0,0), color);
  }

  public void closeForm(double x, double y, Rotate rotation, Color color) {
    closeForm(x, y, 1, 1, rotation, color);
  }

  public double[] getPoints() {
    double[] pos = { getX(), getY(), maxXProperty().get(), getY(), maxXProperty().get(), maxYProperty().get(),
        getX(), maxYProperty().get() };
    // double[] pos = { _anchors.get(0).getCenterX(), _anchors.get(0).getCenterY(), _anchors.get(1).getCenterX(), _anchors.get(1).getCenterY(),
    //   _anchors.get(2).getCenterX(), _anchors.get(2).getCenterY(), _anchors.get(3).getCenterX(), _anchors.get(3).getCenterY() };
    
    return pos;
  }

  public double[] getPointsInParent() {
    return localToParent(getPoints());
  }

  public double[] getNoRotatedParentPos() {
    double[] rotated = getPointsInParent();
    double[] pos = new double[rotated.length];

    // All points by clock wise
    pos[0] = rotated[0];
    pos[1] = rotated[1];
    pos[2] = pos[0] + getWidth();
    pos[3] = pos[1];
    pos[4] = pos[2];
    pos[5] = pos[1] + getHeight();
    pos[6] = pos[0];
    pos[7] = pos[5];

    return pos;
  }

  public double[] noRotatedParentPointsToRotated(double pos[]) {
    double[] rotated = new double[pos.length];
    double[] rect = getPointsInParent();

    Rotate rotate = new Rotate(getRotation().getAngle(), rect[0], rect[1]);
    
    for (int i = 0; i < pos.length; i += 2) {
      Point2D pt = rotate.transform(pos[i], pos[i + 1]);
      rotated[i] = pt.getX();
      rotated[i + 1] = pt.getY();
    }

    return rotated;
  }
}