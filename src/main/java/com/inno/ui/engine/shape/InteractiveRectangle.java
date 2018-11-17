/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 17th November 2018
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

public class InteractiveRectangle extends InteractiveShape {
  private ArrayList<Line> _lines = new ArrayList<>();
  private Rectangle _rectangle = null;
  private Circle _cursor = null;
  private ObservableList<Anchor> _anchors = null;
  private boolean _collisionDetected = false;
  private Group _group;

  private DoubleProperty xProperty = null;
  private DoubleProperty yProperty = null;
  private DoubleProperty widthProperty = null;
  private DoubleProperty heightProperty = null;
  private DoubleProperty xPropertyRU = null;
  private DoubleProperty xPropertyRD = null;
  private DoubleProperty maxYProperty = null;


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
        // Point2D p = Pane().sceneToLocal(event.getSceneX(), event.getSceneY());
        double newX = event.getX();
        double newY = event.getY();
        // double newXCursor = p.getX();
        // double newYCursor = p.getY();

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

        _collisionDetected = Engine().isObjectUnderCursor(_cursor)
            || (_lines.size() > 0 ? Engine().isObjectUnderCursor(_lines.get(_lines.size() - 1)) : false);
        if (_collisionDetected) {
          if (_lines.size() > 0)
            _lines.get(_lines.size() - 1).setStroke(Color.RED);
          Pane().setCursor(closeCursor);

        } else {
          if (_lines.size() > 0)
            _lines.get(_lines.size() - 1).setStroke(Color.KHAKI);
          Pane().setCursor(addCursor);
        }
        // updateCurrentLine();
      }
    };
    EventHandler<MouseEvent> mouseReleasedEvent = event -> {
      if (_collisionDetected)
        return;
      closeForm(event);
      if (onMouseReleased(event)) {
      }
    };

    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);
  }

  double currentX, currentY, currentWidth, currentHeight;

  private ObservableList<Anchor> createControlAnchorsFor() {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    xProperty = new SimpleDoubleProperty();
    yProperty = new SimpleDoubleProperty();
    xPropertyRU = new SimpleDoubleProperty();
    xPropertyRD = new SimpleDoubleProperty();
    maxYProperty = new SimpleDoubleProperty();
    widthProperty = new SimpleDoubleProperty();
    heightProperty = new SimpleDoubleProperty();

    xProperty.set(_rectangle.getX());
    yProperty.set(_rectangle.getY());
    xPropertyRU.set(_rectangle.getX() + _rectangle.getWidth());
    xPropertyRD.set(_rectangle.getX() + _rectangle.getWidth());
    maxYProperty.set(_rectangle.getY() + _rectangle.getHeight());
    widthProperty.set(_rectangle.getWidth());
    heightProperty.set(_rectangle.getHeight());

    Anchor resizeHandleLU = new Anchor(Color.GOLD, xProperty, yProperty);
    Anchor resizeHandleRU = new Anchor(Color.GOLD, xPropertyRU, yProperty);
    Anchor resizeHandleRD = new Anchor(Color.GOLD, xPropertyRD, maxYProperty);
    Anchor resizeHandleLD = new Anchor(Color.GOLD, xProperty, maxYProperty);

    widthProperty.addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if ((double) newX != _rectangle.getWidth())
        xPropertyRU.set(_rectangle.getX() + (double) newX);
    });

    heightProperty.addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if ((double) newY != _rectangle.getHeight())
        maxYProperty.set(_rectangle.getY() + (double) newY);
    });

    resizeHandleLU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      xProperty.set((double) newX);
      _rectangle.setX((double) newX);
      _rectangle.setWidth(_rectangle.getWidth() + (double) oldX - (double) newX);
      widthProperty.set(_rectangle.getWidth());
    });
    resizeHandleLU.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      yProperty.set((double) newY);
      _rectangle.setY((double) newY);
      _rectangle.setHeight(_rectangle.getHeight() + (double) oldY - (double) newY);
      heightProperty.set(_rectangle.getHeight());
    });

    resizeHandleRU.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      xPropertyRD.set((double) newX);
      _rectangle.setWidth(_rectangle.getWidth() + (double) newX - (double) oldX);
      widthProperty.set(_rectangle.getWidth());
    });
    // resizeHandleRU.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
    // });

    resizeHandleRD.centerXProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      xPropertyRU.set((double) newX);
    });
    resizeHandleRD.centerYProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      maxYProperty.set((double) newY);
      _rectangle.setHeight(_rectangle.getHeight() + (double) newY - (double) oldY);
      heightProperty.set(_rectangle.getHeight());
    });

    anchors.add(resizeHandleLU);
    anchors.add(resizeHandleRU);
    anchors.add(resizeHandleRD);
    anchors.add(resizeHandleLD);

    return anchors;
  }

  class Anchor extends Circle {
    // private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 5);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(1);
      // setStrokeType(StrokeType.OUTSIDE);

      // this.x = x;
      // this.y = y;

      centerXProperty().bindBidirectional(x);
      centerYProperty().bindBidirectional(y);
      // x.bind(centerXProperty());
      // y.bind(centerYProperty());
      enableDrag();
    }

    // Anchor(Color color, double x, double y,
    // DoubleProperty width, DoubleProperty height, DoubleProperty xProperty,
    // DoubleProperty yProperty) {
    // super(_rectangle.getX() + _rectangle.getWidth(), _rectangle.getY() +
    // _rectangle.getHeight(), 5);
    // setFill(color.deriveColor(1, 1, 1, 0.5));
    // setStroke(color);
    // setStrokeWidth(1);
    // // setStrokeType(StrokeType.OUTSIDE);

    // // this.x = x;
    // // this.y = y;
    // width.bindBidirectional(xProperty);
    // height.bindBidirectional(yProperty);
    // enableDrag();
    // }

    // public DoubleProperty getX() {
    // return this.x;
    // }

    // public DoubleProperty getY() {
    // return this.y;
    // }

    private void enableDrag() {
      setOnMouseDragged(mouseEvent -> {
        Point2D mousePos = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

        _cursor.setCenterX(mousePos.getX());
        _cursor.setCenterY(mousePos.getY());

        Shape element = Engine().getObjectUnderCursor(_cursor);
        if (element != null) {
          Point2D pos = Engine().getCollisionCenter(_cursor, element, _group);

          // TODO: Collision offset

          setCenterX(pos.getX());
          setCenterY(pos.getY());

          // pos = _group.localToParent(pos.getX(), pos.getY());
          // Circle circle = new Circle(pos.getX(), pos.getY(), 5, Color.TRANSPARENT);
          // circle.setStroke(Color.ALICEBLUE);
          // circle.setStrokeWidth(1);
          // Pane().getChildren().add(circle);
        } else {
          Point2D groupMouse = _group.parentToLocal(mousePos.getX(), mousePos.getY());
          setCenterX(groupMouse.getX());
          setCenterY(groupMouse.getY());
        }

        if (Engine().isObjectUnderCursor(getShape())) {
          _rectangle.setFill(Color.RED);
        } else {
          _rectangle.setFill(Color.GREEN);
        }
      });
    }

    public Shape getShape() {
      return (Circle) this;
    }
  }

  private void select() {
    if (Engine().getSelectedShape() != this) {
      enableShadhow(_rectangle);
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

    disableShadow(_rectangle);
    System.out.println("DESELECTED");
  }

  public Shape getShape() {
    return _rectangle;
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
    return xProperty;
  }

  public DoubleProperty getYProperty() {
    return yProperty;
  }

  public DoubleProperty getMaxXProperty() {
    return xPropertyRU;
  }

  public DoubleProperty getMaxYProperty() {
    return maxYProperty;
  }

  public DoubleProperty getWidthProperty() {
    return widthProperty;
  }

  public DoubleProperty getHeightProperty() {
    return heightProperty;
  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  private void closeForm(MouseEvent event) {
    // Pane().setCursor(Cursor.HAND);
    // System.out.println("close form");
    _rectangle = new Rectangle(event.getX(), event.getY(), 20, 20);
    _rectangle.setFill(Color.ROYALBLUE);
    _rectangle.setOpacity(0.7);

    enableShadhow(_rectangle);

    _anchors = createControlAnchorsFor();

    for (Anchor anchor : _anchors) {
      getSelectShapes().add(anchor.getShape());
    }

    // for (int i = 0; i < 4; ++i) {
    // Line line = new Line();

    // line.setStrokeWidth(1.0);
    // line.setStroke(Color.KHAKI);
    // line.setVisible(false);
    // line.setStartX();
    // line.setStartY(_cursor.getCenterY());
    // line.setStrokeLineCap(StrokeLineCap.ROUND);
    // _lines.add(line);
    // Pane().getChildren().add(line);
    // addOutboundShape(line);
    // }

    // Line activeLine = _lines.get(_lines.size() - 1);
    // activeLine.setEndX(firstPoint.getCenterX());
    // activeLine.setEndY(firstPoint.getCenterY());

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
    Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);

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

    Engine().addInteractiveShape(this);

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
    // _group.getTransforms().add(new Rotate(Math.random() * 360 + 1, _rectangle.getX() + _rectangle.getWidth() / 2,
        // _rectangle.getY() + _rectangle.getHeight() / 2));

    // Point2D center = Engine().getCenterOfPoints(points);
    // _group.getTransforms().add(new Rotate(Math.random() * 360 + 1, center.getX(),
    // center.getY()));

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

        Polygon shape = new Polygon();
        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(Color.WHITE);
        // shape.getPoints().addAll(_rectangle.getPoints());
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
        if (!Engine().isObjectUnderCursor(shape)) {
          _group.setTranslateX(newTranslateX);
          _group.setTranslateY(newTranslateY);
        }
        Pane().getChildren().remove(shape);
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    Engine().selected(this);

    return;
  }

  private void updateCursor(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
  }

  private void enableShadhow(Shape shape) {
    int depth = 10;
    DropShadow borderGlow = new DropShadow();
    borderGlow.setOffsetY(0f);
    borderGlow.setOffsetX(0f);
    borderGlow.setColor(Color.GOLD);
    borderGlow.setWidth(depth);
    borderGlow.setHeight(depth);

    shape.setEffect(borderGlow);
  }

  private void disableShadow(Shape shape) {
    shape.setEffect(null);
  }

  public void destroy() {
    Pane().getChildren().remove(_group);
  }
}