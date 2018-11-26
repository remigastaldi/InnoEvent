/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 25th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import java.util.ArrayList;

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
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class InteractiveRectangle extends InteractiveShape<Rectangle> {
  private boolean _collisionDetected = false;

  private DoubleProperty maxXProperty = null;
  private DoubleProperty maxYProperty = null;

  public InteractiveRectangle(Engine engine, Pane pane) {
    super(engine, pane);

    // setID(id);
  }

  public InteractiveRectangle(Engine engine, Pane pane, String id, double x, double y, double width, double height, double rotation, Color color) {
    super(engine, pane, rotation);

    setID(id);
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
        if (!onFormComplete())
          return;
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

    CircleAnchor resizeHandleLU = new CircleAnchor(Engine(), Color.GOLD, _shape.xProperty(), _shape.yProperty(), true);
    CircleAnchor resizeHandleRU = new CircleAnchor(Engine(), Color.GOLD, maxXProperty, _shape.yProperty(), true);
    CircleAnchor resizeHandleRD = new CircleAnchor(Engine(), Color.GOLD, maxXProperty, maxYProperty, true);
    CircleAnchor resizeHandleLD = new CircleAnchor(Engine(), Color.GOLD, _shape.xProperty(), maxYProperty, true);

    _shape.widthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (_shape.getX() + (double) newX != maxXProperty.get()) {
        maxXProperty.set(_shape.getX() + (double) newX);
      }
    });
    
    _shape.heightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (_shape.getY() + (double) newY != maxYProperty.get())
        maxYProperty.set(_shape.getY() + (double) newY);
    });

    resizeHandleLU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      _shape.setX((double) newX);
      _shape.setWidth(maxXProperty.get() - _shape.getX());
    });
    resizeHandleLU.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      _shape.setY((double) newY);
      _shape.setHeight(maxYProperty.get() - _shape.getY());
    });

    resizeHandleRU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      _shape.setWidth(maxXProperty.get() - _shape.getX());
    });

    resizeHandleRD.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      _shape.setHeight(maxYProperty.get() - _shape.getY());
    });

    anchors.add(resizeHandleLU);
    anchors.add(resizeHandleRU);
    anchors.add(resizeHandleRD);
    anchors.add(resizeHandleLD);

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

  Pane().getChildren().add(line); // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    addOutboundShape(line);
  }

  public void setPositions(double[] pos) {
    setX(pos[0]);
    setY(pos[1]);
    setWidth(pos[2] - pos[0]);
    setHeight(pos[7] - pos[3]);
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

  public DoubleProperty getXProperty() {
    return _shape.xProperty();
  }

  public DoubleProperty getYProperty() {
    return _shape.yProperty();
  }

  public DoubleProperty getMaxXProperty() {
    return maxXProperty;
  }

  public DoubleProperty getMaxYProperty() {
    return maxYProperty;
  }

  public DoubleProperty getWidthProperty() {
    return _shape.widthProperty();
  }

  public DoubleProperty getHeightProperty() {
    return _shape.heightProperty();
  }

  private void closeForm(double x, double y, double width, double height, double rotation, Color color) {
    _shape = new Rectangle(x, y, width, height);
    _shape.setFill(color.deriveColor(1, 1, 0.8, 0.85));

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    if (mouseMovedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mousePressedEvent = EventHandlers().remove(MouseEvent.MOUSE_PRESSED);
    if (mousePressedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedEvent);

    _anchors = createRectangleAnchors();

    Pane().setCursor(Cursor.DEFAULT);

    
    completeShape();
    setColor(color);
    setRotation(rotation, new Point2D( _shape.getX() + _shape.getWidth(), _shape.getY() + _shape.getHeight()));

    // enableGlow();
    Engine().getMagnetismManager().registerInteractiveShape(this);
    
    enableSelection();
    select();

    // Engine().selected(this);

    return;
  }

  private void closeForm(double x, double y) {
    closeForm(x, y, 1, 1, 0, Color.ROYALBLUE);
  }
}