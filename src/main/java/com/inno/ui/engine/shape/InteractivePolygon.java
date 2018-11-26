/*
 * File Created: Sunday, 14th October 2018
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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class InteractivePolygon extends InteractiveShape<Polygon> {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  

  public InteractivePolygon(Engine engine, Pane pane) {
    super(engine, pane);
  }

  public InteractivePolygon(Engine engine, Pane pane, double[] pos, Rotate rotation, Color color) {
    super(engine, pane);
    closeForm(pos, rotation, color);
  }
  
  public void start() {
    java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
    Point2D local = Pane().screenToLocal(mouse.x, mouse.y);

    Circle cursorForm = new Circle(local.getX(), local.getY(), 5.0, Color.TRANSPARENT);
    cursorForm.setStroke(Color.GOLD);
    cursorForm.setStrokeWidth(1.0);

    Cursor().setShape(cursorForm);
    Cursor().setForm(CustomCursor.Type.ADD);
    // _cursor = new CustomCursor(Pane(), local.getX(), local.getY(), 5.0);


    EventHandler<MouseEvent> mouseReleasedEvent = event -> {
      if (onMouseReleased(event)) {
        if (_points.size() > 0 && Cursor().getBoundShape().intersects(_points.get(0).getBoundsInParent())) {
          closeForm();
        } else {
          // if (!_collisionDetected)
          addPoint();
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedEvent);

    EventHandler<MouseEvent> mouseMovedEvent = event -> {
      if (onMouseMoved(event)) {
        Point2D pos = Engine().getMagnetismManager().checkMagnetism(Cursor().getBoundShape());

        if (pos != null) {
          Cursor().setX(pos.getX());
          Cursor().setY(pos.getY());
        } else {
          updateCursor(event);
        }

        // _collisionDetected = Engine().isObjectUnderCursor(_cursor)
        //     || (_lines.size() > 0 ? Engine().isObjectUnderCursor(_lines.get(_lines.size() - 1)) : false);
        // if (_collisionDetected) {
        //   if (_lines.size() > 0)
        //     _lines.get(_lines.size() - 1).setStroke(Color.RED);
        //   Pane().setCursor(closeCursor);

        // } else {
        //   if (_lines.size() > 0)
        //     _lines.get(_lines.size() - 1).setStroke(Color.KHAKI);
        //   Pane().setCursor(addCursor);
        // }
        updateCurrentLine();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
  }

  private void addPoint() {
    Line line = new Line();

    line.setStrokeWidth(1.0);
    line.setStroke(Color.KHAKI);
    line.setVisible(false);
    line.setStartX(Cursor().getX());
    line.setStartY(Cursor().getY());
    line.setStrokeLineCap(StrokeLineCap.ROUND);
    _lines.add(line);
    Pane().getChildren().add(line);
    addOutboundShape(line);

    Circle circle = new Circle(Cursor().getX(), Cursor().getY(), 5.0);
    circle.setFill(Color.GREEN);

    _points.add(circle);
    Pane().getChildren().add(circle);
  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  private void closeForm(double[] pos, Rotate rotation, Color color) {

  }

  private void closeForm() {
    _shape = new Polygon();
    // _shape.setFill(color.deriveColor(1, 1, 0.8, 0.85));

    Line activeLine = _lines.get(_lines.size() - 1);
    Circle firstPoint = _points.get(0);
    activeLine.setEndX(firstPoint.getCenterX());
    activeLine.setEndY(firstPoint.getCenterY());

    ArrayList<Point2D> points = new ArrayList<>();
    for (Circle point : _points) {
      points.add(new Point2D(point.getCenterX(), point.getCenterY()));
      Pane().getChildren().remove(point);
    }
    
    _shape.setFill(Color.GOLDENROD);
    for (Circle point : _points) {
      _shape.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
    }
    _points = null;

    enableGlow();

    _anchors = createControlAnchorsFor(_shape.getPoints());

    for (CircleAnchor anchor : _anchors) {
      getSelectShapes().add(anchor.getShape());
    }

    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
    Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);

    Cursor().setForm(CustomCursor.Type.DEFAULT);
    Cursor().removeShape();

    // Add form selection callback
    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        select();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_CLICKED, mouseClick);
    _shape.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);

    ArrayList<Node> nodes = new ArrayList<>();
    nodes.add(_shape);
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

    // setColor(color);

    // Point2D center = Engine().getCenterOfPoints(points);
    // _group.getTransforms().add(new Rotate(Math.random() * 360 + 1, center.getX(), center.getY()));
    


    _group.setOnMousePressed(mouseEvent -> { 
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group)(_group)).getTranslateX();
      orgTranslateY = ((Group)(_group)).getTranslateY();
    });

    EventHandler<MouseEvent> mouseDragged = event -> {
      if (onMouseMoved(event)) {
        select();
        // TODO: Magnetism between anchors and lines

        // Polygon shape = new Polygon();
        // shape.setFill(Color.TRANSPARENT);
        // shape.setStroke(Color.WHITE);
        // shape.getPoints().addAll(_shape.getPoints());
        // ObservableList<Transform> effect = _group.getTransforms();
        // if (effect != null && effect.size() > 0)
        //   shape.getTransforms().add(effect.get(0));
  
        Point2D p = Pane().sceneToLocal(event.getSceneX(), event.getSceneY());
        double offsetX = p.getX() - orgSceneX;
        double offsetY = p.getY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        // Pane().getChildren().add(shape);
        // shape.setTranslateX(newTranslateX);
        // shape.setTranslateY(newTranslateY);

        // TMP
      _group.setTranslateX(newTranslateX);
        _group.setTranslateY(newTranslateY);
        // if (!Engine().isObjectUnderCursor(shape)) {
        //   // _group.setTranslateX(newTranslateX);
        //   // _group.setTranslateY(newTranslateY);
        //   _shape.setFill(Color.ROYALBLUE);
        // } else
        // _shape.setFill(Color.RED);
        // Pane().getChildren().remove(shape);
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    Engine().addInteractiveShape(this);
    Engine().selected(this);
    Engine().getMagnetismManager().registerInteractiveShape(this);

    return;
  }

  private void updateCursor(MouseEvent event) {
    Cursor().setX(event.getX());
    Cursor().setY(event.getY());
  }

  private void updateCurrentLine() {
    if (_points == null || _points.size() == 0)
      return;

    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    activeLine.setEndX(Cursor().getX());
    activeLine.setEndY(Cursor().getY());
  }

  private ObservableList<CircleAnchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<CircleAnchor> anchors = FXCollections.observableArrayList();

    int j = 0;
    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;
      final int idj = j;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener((ChangeListener<Number>) (ov, oldX, x) -> {
        points.set(idx, x.doubleValue());
        _lines.get(idj - 1 < 0 ? _lines.size() - 1 : idj - 1).setEndX((double) x);
        _lines.get(idj).setStartX((double) x);
      });

      yProperty.addListener((ChangeListener<Number>) (ov, oldY, y) -> {
        points.set(idx + 1, y.doubleValue());
        _lines.get(idj - 1 < 0 ? _lines.size() - 1 : idj - 1).setEndY((double) y);
        _lines.get(idj).setStartY((double) y);
      });
      j++;
      anchors.add(new CircleAnchor(Engine(), Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  public void deselect() {
    for (Circle point : _points) {
      point.setVisible(false);
    }
    for (Shape selectShape : getSelectShapes()) {
      selectShape.setVisible(false);
    }
    
    disableGlow();
    System.out.println("DESELECTED");
  }
  
  public Shape getShape() {
    return _shape;
  }

  public void destroy() {
    Pane().getChildren().remove(_group);
    onDestroy();
  }
}