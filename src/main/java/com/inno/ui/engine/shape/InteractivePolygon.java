/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 29th November 2018
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;

public class InteractivePolygon extends InteractiveShape<Polygon> {
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
        if (getAdditionalShapes().size() > 0 && Cursor().getBoundShape().intersects(getAdditionalShapes().get(0).getBoundsInParent())) {
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
        //     || (lines.size() > 0 ? Engine().isObjectUnderCursor(lines.get(lines.size() - 1)) : false);
        // if (_collisionDetected) {
        //   if (lines.size() > 0)
        //     lines.get(lines.size() - 1).setStroke(Color.RED);
        //   Pane().setCursor(closeCursor);

        // } else {
        //   if (lines.size() > 0)
        //     lines.get(lines.size() - 1).setStroke(Color.KHAKI);
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

  private Line createLine(double startX, double startY, double endX, double endY) {
    Line line = new Line();

    line.setStrokeWidth(1.0);
    line.setStroke(Color.KHAKI);
    line.setStartX(startX);
    line.setStartY(startY);
    line.setEndX(endX);
    line.setEndY(endY);
    line.setStrokeLineCap(StrokeLineCap.ROUND);
  
    return line;
  }

  private void addPoint() {
    Circle circle = new Circle(Cursor().getX(), Cursor().getY(), 5.0);
    circle.setFill(Color.GREEN);

    addAdditionalShape(circle);

    Line line = createLine(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY());
    addOutboundShape(line);
  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  private void closeForm(double[] pos, Rotate rotation, Color color) {
    setShape(new Polygon(pos));
    
    _shape.setFill(color.deriveColor(1, 1, 0.8, 0.85));

    for (int i = 0; i < pos.length; i+=2) {
      Line line = createLine(pos[i], pos[i + 1], pos[i + 2 < pos.length ? i + 2 : 0], pos[i + 3 < pos.length ? i + 3 : 1]);
      addOutboundShape(line);
    }

    _anchors = createPolygonAnchor(_shape.getPoints());

    finialisePolygon(color);
  }

  private void closeForm(Color color) {
    setShape(new Polygon());

    ArrayList<Shape> lines = getOutBoundShapes();

    Line activeLine = (Line) lines.get(lines.size() - 1);
    Line firstPoint = (Line) lines.get(0);
    activeLine.setEndX(firstPoint.getStartX());
    activeLine.setEndY(firstPoint.getStartY());

    clearAdditionalShape();

    for (Shape shape : lines) {
      Line line = (Line) shape;
      _shape.getPoints().addAll(new Double[] { line.getStartX(), line.getStartY() });
    }
    
    EventHandler<MouseEvent> mouseMovedEvent = EventHandlers().remove(MouseEvent.MOUSE_MOVED);
    Pane().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandler<MouseEvent> mouseRelesedEvent = EventHandlers().remove(MouseEvent.MOUSE_RELEASED);
    Pane().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseRelesedEvent);
    EventHandler<MouseEvent> mouseDraggEvent = EventHandlers().remove(MouseEvent.MOUSE_DRAGGED);
    Pane().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggEvent);
  
    _anchors = createPolygonAnchor(_shape.getPoints());

    finialisePolygon(color);

    onFormComplete();

    return;
  }
  
  // TODO: TMP
  private void closeForm() {
    closeForm(Color.ROYALBLUE);
  }

  private void finialisePolygon(Color color) {
	  Cursor().setForm(CustomCursor.Type.DEFAULT);
    Cursor().removeShape();

    // completeShape();
    setColor(color);  
    
    // Point2D center = Engine().getCenterOfPoints(points);
    // setRotation(0.0, center.getX(), center.getY());
  
    Engine().getMagnetismManager().registerInteractiveShape(this);

    enableSelection();
    select();

    // System.out.println(getShape().get)
  }

  private void updateCursor(MouseEvent event) {
    Cursor().setX(event.getX());
    Cursor().setY(event.getY());
  }

  private void updateCurrentLine() {
    ArrayList<Shape> lines = getOutBoundShapes();

    if (lines == null || lines.size() == 0)
      return;

    Line activeLine = (Line)lines.get(lines.size() - 1);
    activeLine.setVisible(true);

    activeLine.setEndX(Cursor().getX());
    activeLine.setEndY(Cursor().getY());
  }

  private ObservableList<CircleAnchor> createPolygonAnchor(final ObservableList<Double> points) {
    ObservableList<CircleAnchor> anchors = FXCollections.observableArrayList();

    int j = 0;
    ArrayList<Shape> lines = getOutBoundShapes();

    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));


      ((Line)lines.get(j - 1 < 0 ? lines.size() - 1 : j - 1)).endXProperty().bind(xProperty);
      ((Line)lines.get(j - 1 < 0 ? lines.size() - 1 : j - 1)).endYProperty().bind(yProperty);
      ((Line)lines.get(j)).startXProperty().bind(xProperty);
      ((Line)lines.get(j)).startYProperty().bind(yProperty);
      xProperty.addListener((ChangeListener<Number>) (ov, oldX, x) -> {
        points.set(idx, x.doubleValue());
      });

      yProperty.addListener((ChangeListener<Number>) (ov, oldY, y) -> {
        points.set(idx, y.doubleValue());
      });
      CircleAnchor circleAnchor = new CircleAnchor(Engine(), this, Color.GOLD, xProperty, yProperty);
      anchors.add(circleAnchor);
      addSelectShape(circleAnchor);
      j++;
    }

    return anchors;
  }

  @Override
  public void setPoints(double[] points) {
    _shape.getPoints().clear();
    for (double point : points) {
      _shape.getPoints().add(point);
    }
  }

  @Override
  public double[] getPoints() {
    double[] pos = new double[_anchors.size() * 2];
    int i = 0;
    for (CircleAnchor anchor : _anchors) {
      pos[i] = anchor.getCenterX();
      pos[i + 1] = anchor.getCenterY();
      i += 2;
    }
    return pos;
  }

  @Override
  public double[] getPointsInParent() {
    return localToParent(getPoints());
  }  
}