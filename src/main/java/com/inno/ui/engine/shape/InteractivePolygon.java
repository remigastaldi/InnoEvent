/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 12th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import java.util.ArrayList;
import java.util.HashMap;

import com.inno.ui.engine.Engine;

import javafx.scene.layout.Pane;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.paint.Color;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.collections.FXCollections;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.*;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollBar;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Rotate;
import javafx.geometry.Dimension2D;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BorderWidths;
import javafx.scene.effect.DropShadow;

public class InteractivePolygon extends InteractiveShape {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  private Polygon _polygon = null;
  private Circle _cursor = null;
  private ObservableList<Anchor> _anchors = null;
  private boolean _collisionDetected = false;
  InteractivePolygon _this = this;

  private Group group;

  private boolean grabbed = false;

  public InteractivePolygon(Engine engine, Pane pane) {
    super(engine, pane);
  }

  public void start() {
    // Bound check cursor
    _cursor = new Circle();
    _cursor.setFill(Color.TRANSPARENT);
    _cursor.setRadius(5.0);
    _cursor.setStroke(Color.GREEN);
    _cursor.setStrokeWidth(2);
    Pane().getChildren().add(_cursor);

    // System cursor
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    Image addIcon = new Image("icon/add.png");
    Image closeIcon = new Image("icon/close.png");
    //TODO: mac problem ??
    // Dimension2D addSizes = ImageCursor.getBestSize(addIcon.getWidth(), addIcon.getHeight());
    // Dimension2D closeSizes = ImageCursor.getBestSize(addIcon.getWidth(), addIcon.getHeight());
    ImageCursor addCursor = new ImageCursor(addIcon, addIcon.getWidth() / 2, addIcon.getHeight() / 2);
    ImageCursor closeCursor = new ImageCursor(closeIcon, closeIcon.getWidth() / 2, closeIcon.getHeight() / 2);
    Pane().setCursor(addCursor);

    _cursor.setVisible(false);
    // Pane().setCursor(Cursor.NONE);

    EventHandler<MouseEvent> mouseClickEvent = event -> {
      if (onMouseClicked(event)) {
        if (_points.size() > 0 && _cursor.intersects(_points.get(0).getBoundsInParent())) {
          closeForm();
        } else {
          // if (!_collisionDetected)
          addPoint(event);
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseClickEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseClickEvent);

    EventHandler<MouseEvent> mouseMovedEvent = event -> {
      if (onMouseMoved(event)) {
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
        updateCursor(event);
        updateCurrentLine(event);
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
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

  private void addPoint(MouseEvent event) {
    Line line = new Line();

    line.setStrokeWidth(1.0);
    line.setStroke(Color.KHAKI);
    line.setVisible(false);
    line.setStartX(event.getX());
    line.setStartY(event.getY());
    _lines.add(line);
    Pane().getChildren().add(line);
    // TEST
    addOutboundShape(line);

    Circle circle = new Circle(event.getX(), event.getY(), 5.0);
    circle.setFill(Color.GREEN);

    _points.add(circle);
    Pane().getChildren().add(circle);
  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  private void closeForm() {
    // Pane().setCursor(Cursor.HAND);
    System.out.println("close form");
    _polygon = new Polygon();

    ArrayList<Point2D> points = new ArrayList<>();
    for (Circle point : _points) {
      points.add(new Point2D(point.getCenterX(), point.getCenterY()));
    }
    Point2D center = Engine().getCenterOfPoints(points);

    _polygon.setFill(Color.GOLDENROD);
    for (Circle point : _points) {
      _polygon.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
    }

    enableShadhow(_polygon);
    _polygon.setOpacity(0.7);

    Pane().getChildren().add(_polygon);
    _anchors = createControlAnchorsFor(_polygon.getPoints());
    for (Anchor anchor : _anchors) {
      _extShapes.add(anchor.getShape());
    }

    for (Circle point : _points) {
      point.setVisible(false);
    }

    Line activeLine = _lines.get(_lines.size() - 1);
    Circle firstPoint = _points.get(0);
    activeLine.setEndX(firstPoint.getCenterX());
    activeLine.setEndY(firstPoint.getCenterY());

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
    Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);

    Pane().setCursor(Cursor.DEFAULT);

    // Add form selection callback
    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        select();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_CLICKED, mouseClick);
    _polygon.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);

    Engine().addInteractiveShape(this);

    ArrayList<Node> nodes = new ArrayList<>();
    Pane().getChildren().remove(_polygon);
    nodes.add(_polygon);
    for (Shape outBound : getOutBoundShapes()) {
      Pane().getChildren().remove(outBound);
      nodes.add(outBound);
    }
    for (Shape extShapes : getExtShapes()) {
      Pane().getChildren().remove(extShapes);
      nodes.add(extShapes);
    }
    group = new Group(nodes);
    group.getTransforms().add(new Rotate(90, center.getX(), center.getY()));
    Pane().getChildren().add(group);
    


    group.setOnMousePressed(mouseEvent -> { 
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group)(group)).getTranslateX();
      orgTranslateY = ((Group)(group)).getTranslateY();
    });


    EventHandler<MouseEvent> mouseDragged = event -> {
      if (onMouseMoved(event)) {
        Point2D p = Pane().sceneToLocal(event.getSceneX(), event.getSceneY());
        double offsetX = p.getX() - orgSceneX;
        double offsetY = p.getY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        
        ((Group)(group)).setTranslateX(newTranslateX);
        ((Group)(group)).setTranslateY(newTranslateY);
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _polygon.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    Engine().selected(this);

    return;
  }

  private void updateCursor(MouseEvent event) {
    // System.out.println(event.getSceneX() + " " + event.getX());
    // System.out.println(cet + " " + newY);
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
  }

  private void updateCurrentLine(MouseEvent event) {
    if (_points.size() == 0)
      return;

    // if (_cursor.intersects(_points.get(0).getBoundsInParent())) {
    // Pane().setCursor(Cursor.HAND);
    // } else if (Pane().get_cursor == Cursor.HAND)
    // Pane().setCursor(Cursor.DEFAULT);

    // Circle lastPoint = _points.get(_points.size() - 1);
    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }

  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    int j = 0;
    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;
      final int idj = j;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener((ChangeListener<Number>) (ov, oldX, x) -> {
        points.set(idx, (double) x);
        _lines.get(idj - 1 < 0 ? _lines.size() - 1 : idj - 1).setEndX((double) x);
        _lines.get(idj).setStartX((double) x);
      });

      yProperty.addListener((ChangeListener<Number>) (ov, oldY, y) -> {
        points.set(idx + 1, (double) y);
        _lines.get(idj - 1 < 0 ? _lines.size() - 1 : idj - 1).setEndY((double) y);
        _lines.get(idj).setStartY((double) y);
      });
      j++;
      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  class Anchor extends Circle {
    private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 5);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(1);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    public DoubleProperty getX() {
      return this.x;
    }

    public DoubleProperty getY() {
      return this.y;
    }

    private void enableDrag() {
      final Delta dragDelta = new Delta();
      // setOnMousePressed(mouseEvent -> {
        // record a delta distance for the drag and drop operation.
        // dragDelta.x = getCenterX() - mouseEvent.getX();
        // dragDelta.y = getCenterY() - mouseEvent.getY();
        // getScene().setCursor(Cursor.MOVE);
      // });
      setOnMouseDragged(mouseEvent -> {
        Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        // System.out.println("Deltax " + dragDelta.x);
        double newX = mouseEvent.getX() + dragDelta.x;
        double newY = mouseEvent.getY() + dragDelta.y;
        double newXCursor = p.getX();
        double newYCursor = p.getY();

        // updateCursor(mouseEvent);
        // Circle tmp = null;
        Circle tmp = _cursor;
        if (grabbed) {
          tmp = new Circle(newXCursor, newYCursor, 6.5, Color.TRANSPARENT);
        } else {
          Point2D pos2 = group.localToParent(getCenterX(), getCenterY());
          tmp = new Circle(pos2.getX(), pos2.getY(), 6.5, Color.TRANSPARENT);
        }
        tmp.setStrokeWidth(1);
        tmp.setStroke(Color.WHITE);
        // tmp.setStrokeType(StrokeType.OUTSIDE);
        Pane().getChildren().add(tmp);
        // _collisionDetected = _this.Engine().isObjectUnderCursor(tmp);
        Shape element = _this.Engine().getObjectUnderCursor(tmp);
        // _this._collisionDetected = _this.Engine().isObjectUnderCursor(tmp);
        if (element != null) {
          Point2D pos = Engine().getCollisionShape(tmp, element, group);
          Point2D pos2 = group.localToParent(getCenterX(), getCenterY());
          Circle test = new Circle(pos2.getX(), pos2.getY(), 6.5, Color.TRANSPARENT);

          if (test.getCenterX() > pos.getX()) {
            setCenterX(pos.getX() + 1.5);
          } else {
            setCenterX(pos.getX() - 1.5);
          }
          if (test.getCenterY() > pos.getY()) {
            setCenterY(pos.getY() + 1.5);
          } else {
            setCenterY(pos.getY() - 1.5);
          }

          // setCenterX(pos.getX());
          // setCenterY(pos.getY());

          grabbed = true;

          setStroke(Color.GOLDENROD);
        } else {
          setStroke(Color.GOLD);
          // if (newX > 0 && newX < getScene().getWidth()) {
          setCenterX(newX);
          // }
          // if (newY > 0 && newY < getScene().getHeight()) {
          setCenterY(newY);
          // }
          grabbed = false;
        }
        if (_this.Engine().isObjectUnderCursor(_this.getShape())) {
          _polygon.setFill(Color.RED);
        } else {
          _polygon.setFill(Color.GREEN);
        }
        Pane().getChildren().remove(tmp);
      });
    }

    public Shape getShape() {
      return (Circle) this;
    }
  }

  private void select() {
    if (Engine().getSelectedShape() != this) {
      enableShadhow(_polygon);
      for (Shape outBound : getOutBoundShapes()) {
        outBound.setVisible(true);
      }
      for (Shape extShapes : getExtShapes()) {
        extShapes.setVisible(true);
      }      
      Engine().selected(this);
      System.out.println("SHAPE" + this + " SELECTED");
    }
  }

  public void deselect() {
    for (Circle point : _points) {
      point.setVisible(false);
    }

    for (Shape outBound : getOutBoundShapes()) {
      outBound.setVisible(false);
    }
    for (Shape extShapes : getExtShapes()) {
      extShapes.setVisible(false);
    }

    disableShadow(_polygon);
    System.out.println("DESELECTED");
  }

  private class Delta {
    double x, y;
  }

  // --------------- TODO remove theses
  public Shape getShape() {
    return _polygon;
  }

  public Circle getCursor() {
    return _cursor;
  }

  // ---------------
}