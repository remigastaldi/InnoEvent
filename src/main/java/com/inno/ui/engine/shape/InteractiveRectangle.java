/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 23rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import java.util.ArrayList;

import com.inno.app.Core;
import com.inno.app.room.ImmutableScene;
import com.inno.ui.View;
import com.inno.ui.engine.CircleAnchor;
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
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Rotate;
import javafx.beans.binding.DoubleBinding;

public class InteractiveRectangle extends InteractiveShape<Rectangle> {
  private Rectangle _rectangle = null;
  private Circle _cursor = null;
  private ObservableList<CircleAnchor> _anchors = null;
  private boolean _collisionDetected = false;
  private Group _group;

  private DoubleProperty maxXProperty = null;
  private DoubleProperty maxYProperty = null;

  // TMP
  private ChangeListener<Number> listenerX = null;
  private ChangeListener<Number> listenerY = null;

  public InteractiveRectangle(Engine engine, Pane pane) {
    super(engine, pane);
  }

  public void start() {
    java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
    Point2D local = Pane().screenToLocal(mouse.x, mouse.y);

    _cursor = new Circle(local.getX(), local.getY(), 5.0, Color.TRANSPARENT);
    _cursor.setStrokeWidth(1);
    _cursor.setStroke(Color.GREEN);

    Pane().getChildren().add(_cursor);

    // System cursor
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    Image addIcon = new Image("icon/add.png");
    Image closeIcon = new Image("icon/close.png");
    // TODO: mac cursor size problem ??
    // Dimension2D addSizes = ImageCursor.getBestSize(addIcon.getWidth(),
    // addIcon.getHeight());
    // Dimension2D closeSizes = ImageCursor.getBestSize(addIcon.getWidth(),
    // addIcon.getHeight());
    ImageCursor addCursor = new ImageCursor(addIcon, addIcon.getWidth() / 2, addIcon.getHeight() / 2);
    ImageCursor closeCursor = new ImageCursor(closeIcon, closeIcon.getWidth() / 2, closeIcon.getHeight() / 2);
    Pane().setCursor(addCursor);

    EventHandler<MouseEvent> mouseMovedEvent = event -> {
      if (onMouseMoved(event)) {
        double newX = event.getX();
        double newY = event.getY();

        _cursor.setCenterX(newX);
        _cursor.setCenterY(newY);
        Shape element = Engine().getObjectUnderCursor(_cursor);
        if (element != null) {
          Point2D pos = Engine().getCollisionCenter(_cursor, element);

          pos = Pane().sceneToLocal(pos.getX(), pos.getY());
          _cursor.setCenterX(pos.getX());
          _cursor.setCenterY(pos.getY());
        } else {
          updateCursor(event);
        }
      }
    };
    
    EventHandler<MouseEvent> mouseDraggedEvent = event -> {
      if (onMouseOnDragDetected(event)) {
        double newX = event.getX();
        double newY = event.getY();

        _cursor.setCenterX(newX);
        _cursor.setCenterY(newY);
        Shape element = Engine().getObjectUnderCursor(_cursor);
        if (element != null) {
          Point2D pos = Engine().getCollisionCenter(_cursor, element);

          pos = Pane().sceneToLocal(pos.getX(), pos.getY());
          _cursor.setCenterX(pos.getX());
          _cursor.setCenterY(pos.getY());
        } else {
          updateCursor(event);
        }
      }
    };
    EventHandler<MouseEvent> mouseReleasedEvent = event -> {
      if (onMouseReleased(event)) {
        if (_collisionDetected)
          return;
        EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
        Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);
        EventHandler<MouseEvent> mouseReleaseEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
        Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleaseEvent);
        _cursor.centerXProperty().removeListener(listenerX);
        _cursor.centerYProperty().removeListener(listenerY);

        if (!onFormComplete())
          return;
      }
    };
    EventHandler<MouseEvent> mousePressedEvent = event -> {
      if (onMousePressed(event)) {
        if (_collisionDetected)
          return;
        closeForm(event);
        listenerX = (ChangeListener<Number>) (ov, oldX, newX) -> {
          maxXProperty.set(_cursor.getCenterX());
        };
        _cursor.centerXProperty().addListener(listenerX);
        
        listenerY = (ChangeListener<Number>) (ov, oldY, newY) -> {
          maxYProperty.set(_cursor.getCenterY());
        };
        _cursor.centerYProperty().addListener(listenerY);
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

  private void closeForm(MouseEvent event) {
    // Pane().setCursor(Cursor.HAND);
    // System.out.println("close form");
    _rectangle = new Rectangle(event.getX(), event.getY(), 1, 1);
    _shape = _rectangle;
    _rectangle.setFill(Color.ROYALBLUE);
    _rectangle.setOpacity(0.7);

    enableGlow();

    _anchors = createControlAnchorsFor();

    for (CircleAnchor anchor : _anchors) {
      getSelectShapes().add(anchor.getShape());
    }

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    // EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    // Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    EventHandler<MouseEvent> mousePressedEvent = EventHandlers().remove(MouseEvent.MOUSE_PRESSED);
    Pane().removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedEvent);

    Pane().setCursor(Cursor.DEFAULT);
    _cursor.setVisible(false);

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

    return;
  }

  private void updateCursor(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
  }

  public void destroy() {
    Pane().getChildren().remove(_group);
  }
}