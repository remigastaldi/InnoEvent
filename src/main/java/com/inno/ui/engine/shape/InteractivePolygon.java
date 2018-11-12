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
    Dimension2D addSizes = ImageCursor.getBestSize(addIcon.getWidth(), addIcon.getHeight());
    Dimension2D closeSizes = ImageCursor.getBestSize(addIcon.getWidth(), addIcon.getHeight());
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

    // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    // Pane().getChildren().add(_polygon);
  }

  public Point2D getCenterOfPoints(ArrayList<Point2D> points) {
    double sum1 = 0;
    double sum2 = 0;
    double sum3 = 0;

    for (int i = 0; i < points.size(); ++i) {
      Point2D point1 = points.get(i);
      Point2D point2;
      if (i + 1 == points.size()) {
        point2 = points.get(0);
      } else {
        point2 = points.get(i + 1);
      }
      double val1 = ((point1.getX() * point2.getY()) - (point2.getX() * point1.getY()));
      double val2 = (val1 * (point1.getX() + point2.getX()));
      double val3 = (val1 * (point1.getY() + point2.getY()));
      sum1 += val1;
      sum2 += val2;
      sum3 += val3;
    }

    double air = (sum1 / 2);
    return new Point2D((sum2 / (6 * air)), (sum3 / (6 * air)));
  }

  private void addPoint(MouseEvent event) {
    Line line = new Line();

    line.setStrokeWidth(2.0);
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

  private void closeForm() {
    // Pane().setCursor(Cursor.HAND);
    System.out.println("close form");
    _polygon = new Polygon();
    Polyline polyClone = new Polyline();

    ArrayList<Point2D> points = new ArrayList<>();
    for (Circle point : _points) {
      points.add(new Point2D(point.getCenterX(), point.getCenterY()));
    }
    Point2D center = getCenterOfPoints(points);

    _polygon.setFill(Color.DODGERBLUE);
    for (Circle point : _points) {
      _polygon.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
      double x = point.getCenterX();
      double y = point.getCenterY();

      polyClone.getPoints().addAll(new Double[] { x, y });
    }
    double x = _points.get(0).getCenterX();
    double y = _points.get(0).getCenterY();
    polyClone.getPoints().addAll(new Double[] { x, y });

    // double zoomFactor = 1.20;

    double width = polyClone.getBoundsInLocal().getMaxX() - polyClone.getBoundsInLocal().getMinX();
    double height = polyClone.getBoundsInLocal().getMaxX() - polyClone.getBoundsInLocal().getMinY();
    double polygonW = _polygon.getBoundsInLocal().getMaxX() - _polygon.getBoundsInLocal().getMinX();
    double polygonH = _polygon.getBoundsInLocal().getMaxX() - _polygon.getBoundsInLocal().getMinY();
    System.out.println("W " + width + " H " + height);
    System.out.println("W " + polygonW + " H " + polygonH);
    System.out.println("Center " + center.getX() + " " + center.getY());
    // System.out.println("W " + (center.getX() + (width * (1 - zoomFactor) / 2)) + " H "
    //     + (center.getY() + (height * (1 - zoomFactor) / 2)));

    // Scale scale = new Scale();
    // scale.setX(1.2);
    // scale.setY(1.2);
    // scale.setPivotX(center.getX());
    // scale.setPivotY(center.getY());
    // polyClone.getTransforms().addAll(scale);

    polyClone.setScaleX(1.2);
    polyClone.setScaleY(1.2);
    // polyClone.setTranslateX(-2);
    polyClone.setTranslateY(2);
    polyClone.setStroke(Color.WHITE);
    polyClone.setStrokeWidth(0.1);
    // polyClone.setLayoutX(_polygon.getBoundsInParent().getMinX());
    // polyClone.setTranslateY(polyClone.getLayoutY() *1.5);
    // polyClone.setVisible(false);

    // addOutboundShape(polyClone);
    Pane().getChildren().add(polyClone);
    Pane().getChildren().add(_polygon);
    // createControlAnchorsFor(_polygon.getPoints());
    _anchors = createControlAnchorsFor(_polygon.getPoints());
    for (Anchor anchor : _anchors) {
      _extShapes.add(anchor.getShape());
    }
    // Pane().getChildren().addAll(_anchors);

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
    // EventHandler<MouseEvent> mouseClickedEvent =
    // EventHandlers().remove(MouseEvent.MOUSE_CLICKED);
    // Pane().removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEvent);
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

    select();
    Circle centerCircle = new Circle(center.getX(), center.getY(), 2, Color.BLUEVIOLET);
    Pane().getChildren().add(centerCircle);

    ArrayList<Node> nodes = new ArrayList<>();
    // Pane testPane = new Pane();
    Pane().getChildren().remove(_polygon);
    nodes.add(_polygon);
    // testPane.getChildren().add(_polygon);
    for (Shape outBound : getOutBoundShapes()) {
      Pane().getChildren().remove(outBound);
      nodes.add(outBound);
      // testPane.getChildren().add(outBound);
    }
    for (Shape extShapes : getExtShapes()) {
      Pane().getChildren().remove(extShapes);
      nodes.add(extShapes);
      // testPane.getChildren().add(extShapes);
    }
    group = new Group(nodes);
    // Group group = new Group(testPane);
    // group.getTransforms().add(new Rotate(90, center.getX(), center.getY()));
    Pane().getChildren().add(group);

    // testPane.getChildren().remove(_polygon);
    // Pane().getChildren().add(_polygon);
    // for (Shape outBound : getOutBoundShapes()) {
    // testPane.getChildren().remove(outBound);
    // Pane().getChildren().add(outBound);
    // }
    // for (Shape extShapes : getExtShapes()) {
    // testPane.getChildren().remove(extShapes);
    // Pane().getChildren().add(extShapes);
    // }

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

    // activeLine.setStartX(lastPoint.getCenterX());
    // activeLine.setStartY(lastPoint.getCenterY());
    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }

  private void select() {
    System.out.println("SHAPE" + this + " SELECTED");
    if (Engine().getSelectedShape() != this) {
      System.out.println("SHAPE" + this + " SELECTED");
      Engine().selected(this);
      // Pane().getChildren().addAll(_anchors);
    }
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
      setStrokeWidth(2);
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
      setOnMousePressed(mouseEvent -> {
        // record a delta distance for the drag and drop operation.
        // dragDelta.x = getCenterX() - mouseEvent.getX();
        // dragDelta.y = getCenterY() - mouseEvent.getY();
        // getScene().setCursor(Cursor.MOVE);
      });
      // setOnMouseReleased(new EventHandler<MouseEvent>() {
      // @Override public void handle(MouseEvent mouseEvent) {
      // getScene().setCursor(Cursor.HAND);
      // }
      // });
      // Circle _circle = this;
      setOnMouseDragged(mouseEvent -> {
        Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        // System.out.println("Deltax " + dragDelta.x);
        double newX = mouseEvent.getX() + dragDelta.x;
        double newY = mouseEvent.getY() + dragDelta.y;
        double newXCursor = p.getX();
        double newYCursor = p.getY();

        updateCursor(mouseEvent);
        Circle tmp = null;
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
          setCenterX(pos.getX());
          setCenterY(pos.getY());

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

  public void deselect() {
    for (Circle point : _points) {
      point.setVisible(false);
    }
    System.out.println("DESELECTED");
    // Pane().getChildren().removeAll(_anchors);
    // ArrayList<Node> nodes = new ArrayList<>();
    // for (InteractiveShape shape : Engine().getShapes()) {
    // nodes.add(shape.getShape());
    // for (Shape outBound : shape.getOutBoundShapes()) {
    // nodes.add(outBound);
    // }
    // if (shape == this && Engine().getSelectedShape() == null) {
    // for (Shape extShapes : shape.getExtShapes()) {
    // nodes.add(extShapes);
    // }
    // }
    // }
    // -----------
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