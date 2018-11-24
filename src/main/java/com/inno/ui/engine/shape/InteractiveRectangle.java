/*
 * File Created: Monday, 15th October 2018
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
import javafx.scene.transform.Transform;

public class InteractiveRectangle extends InteractiveShape<Rectangle> {
  private Rectangle _rectangle = null;
  private ObservableList<CircleAnchor> _anchors = null;
  private boolean _collisionDetected = false;

  private DoubleProperty maxXProperty = null;
  private DoubleProperty maxYProperty = null;

  public InteractiveRectangle(Engine engine, Pane pane) {
    super(engine, pane);
  }

  public InteractiveRectangle(Engine engine, Pane pane, String id, double x, double y, double width, double height, double rotation, Color color) {
    super(engine, pane);

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

  private ObservableList<CircleAnchor> createControlAnchorsFor() {
    ObservableList<CircleAnchor> anchors = FXCollections.observableArrayList();

    maxXProperty = new SimpleDoubleProperty();
    maxYProperty = new SimpleDoubleProperty();

    maxXProperty.set(_rectangle.getX() + _rectangle.getWidth());
    maxYProperty.set(_rectangle.getY() + _rectangle.getHeight());

    CircleAnchor resizeHandleLU = new CircleAnchor(Engine(), Color.GOLD, _rectangle.xProperty(), _rectangle.yProperty(), true);
    CircleAnchor resizeHandleRU = new CircleAnchor(Engine(), Color.GOLD, maxXProperty, _rectangle.yProperty(), true);
    CircleAnchor resizeHandleRD = new CircleAnchor(Engine(), Color.GOLD, maxXProperty, maxYProperty, true);
    CircleAnchor resizeHandleLD = new CircleAnchor(Engine(), Color.GOLD, _rectangle.xProperty(), maxYProperty, true);

    _rectangle.widthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (_rectangle.getX() + (double) newX != maxXProperty.get()) {
        maxXProperty.set(_rectangle.getX() + (double) newX);
      }
    });
    
    _rectangle.heightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (_rectangle.getY() + (double) newY != maxYProperty.get())
        maxYProperty.set(_rectangle.getY() + (double) newY);
    });

    resizeHandleLU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      _rectangle.setX((double) newX);
      _rectangle.setWidth(maxXProperty.get() - _rectangle.getX());
    });
    resizeHandleLU.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      _rectangle.setY((double) newY);
      _rectangle.setHeight(maxYProperty.get() - _rectangle.getY());
    });

    resizeHandleRU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      _rectangle.setWidth(maxXProperty.get() - _rectangle.getX());
    });

    resizeHandleRD.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      _rectangle.setHeight(maxYProperty.get() - _rectangle.getY());
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
    line.setStroke(Color.KHAKI);
    line.startXProperty().bind(first.centerXProperty());
    line.startYProperty().bind(first.centerYProperty());
    line.endXProperty().bind(second.centerXProperty());
    line.endYProperty().bind(second.centerYProperty());

    Pane().getChildren().add(line);
    addOutboundShape(line);
  }

  private void select() {
    if (Engine().getSelectedShape() != this) {
      enableGlow();
      for (Shape selectShape : getSelectShapes()) {
        selectShape.toFront();
        selectShape.setVisible(true);
      }
      Engine().selected(this);
      System.out.println("SHAPE" + this + " SELECTED");
    }
  }

  public void deselect() {
    for (Shape selectShape : getSelectShapes()) {
      selectShape.setVisible(false);
    }

    disableGlow();
    System.out.println("DESELECTED");
  }

  public Shape getShape() {
    return _rectangle;
  }

  public void setPositions(double[] pos) {
    setX(pos[0]);
    setY(pos[1]);
    setWidth(pos[2] - pos[0]);
    setHeight(pos[7] - pos[3]);
  }

  public void setX(double x) {
    _rectangle.setX(x);
  }

  public void setY(double y) {
    _rectangle.setY(y);
  }

  public double getX() {
    return _rectangle.getX();
  }

  public double getY() {
    return _rectangle.getY();
  }

  public void setSize(double width, double height) {
    _rectangle.setWidth(width);
    _rectangle.setHeight(height);
  }

  public void setWidth(double width) {
    _rectangle.setWidth(width);
  }

  public void setHeight(double height) {
    _rectangle.setHeight(height);
  }

  public double getWidth() {
    return _rectangle.getWidth();
  }

  public double getHeight() {
    return _rectangle.getHeight();
  }

  public DoubleProperty getXProperty() {
    return _rectangle.xProperty();
  }

  public DoubleProperty getYProperty() {
    return _rectangle.yProperty();
  }

  public DoubleProperty getMaxXProperty() {
    return maxXProperty;
  }

  public DoubleProperty getMaxYProperty() {
    return maxYProperty;
  }

  public DoubleProperty getWidthProperty() {
    return _rectangle.widthProperty();
  }

  public DoubleProperty getHeightProperty() {
    return _rectangle.heightProperty();
  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  private void closeForm(double x, double y, double width, double height, double rotation, Color color) {
    _rectangle = new Rectangle(x, y, width, height);
    _shape = _rectangle;
    _rectangle.setFill(color);
    _rectangle.setOpacity(0.7);

    enableGlow();

    _anchors = createControlAnchorsFor();

    for (CircleAnchor anchor : _anchors) {
      getSelectShapes().add(anchor.getShape());
    }

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    if (mouseMovedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mousePressedEvent = EventHandlers().remove(MouseEvent.MOUSE_PRESSED);
    if (mousePressedEvent != null)
      Pane().removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedEvent);

    Pane().setCursor(Cursor.DEFAULT);

    // Add form selection callback
    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        select();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_CLICKED, mouseClick);
    _rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);


    ArrayList<Node> nodes = new ArrayList<>();
    nodes.add(_rectangle);
    for (Shape outBound : getOutBoundShapes()) {
      Pane().getChildren().remove(outBound);
      nodes.add(outBound);
    }
    for (Shape selectShape : getSelectShapes()) {
      Pane().getChildren().remove(selectShape);
      nodes.add(selectShape);
    }
    _group = new Group(nodes);
    Pane().getChildren().add(_group);
    for (CircleAnchor anchor : _anchors) {
      anchor.setInteractiveShape(this);
    }
    // _group.getTransforms().add(new Rotate(Math.random() * 360 + 1, _rectangle.getX() + _rectangle.getWidth() / 2,
    //     _rectangle.getY() + _rectangle.getHeight() / 2));

    _group.setOnMousePressed(mouseEvent -> {
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group) (_group)).getTranslateX();
      orgTranslateY = ((Group) (_group)).getTranslateY();
    });

    EventHandler<MouseEvent> mouseDragged = mouseEvent -> {
      if (onMouseMoved(mouseEvent)) {
        select();
        // TODO: Magnetism between anchors and lines

        Rectangle shape = new Rectangle(_rectangle.getX(), _rectangle.getY(),
                                        _rectangle.getWidth(), _rectangle.getHeight());
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(Color.WHITE);
        ObservableList<Transform> effect = _group.getTransforms();
        if (effect != null && effect.size() > 0)
          shape.getTransforms().add(effect.get(0));

        Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        double offsetX = p.getX() - orgSceneX;
        double offsetY = p.getY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        Pane().getChildren().add(shape);
        shape.setTranslateX(newTranslateX);
        shape.setTranslateY(newTranslateY);

        // TMP
      _group.setTranslateX(newTranslateX);
        _group.setTranslateY(newTranslateY);
        if (!Engine().isObjectUnderCursor(shape)) {
          // _group.setTranslateX(newTranslateX);
          // _group.setTranslateY(newTranslateY);
          _rectangle.setFill(Color.ROYALBLUE);
        } else
          _rectangle.setFill(Color.RED);
        Pane().getChildren().remove(shape);
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    Engine().addInteractiveShape(this);
    Engine().selected(this);
    Engine().getMagnetismManager().registerInteractiveShape(this);

    return;
  }

  private void closeForm(double x, double y) {
    closeForm(x, y, 1, 1, 0, Color.ROYALBLUE);
  }

  public void destroy() {
    Pane().getChildren().remove(_group);
    onDestroy();
  }
}