/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 1st November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine.shape;

import java.awt.Rectangle;
import  java.util.ArrayList;
import  java.util.HashMap;

import  com.inno.ui.engine.Engine;

import  javafx.scene.layout.Pane;
import  javafx.geometry.Point2D;
import  javafx.scene.Cursor;
import  javafx.scene.input.MouseEvent;
import  javafx.scene.shape.Line;
import  javafx.scene.shape.Circle;
import  javafx.scene.shape.Polygon;
import  javafx.scene.paint.Color;
import  javafx.collections.ObservableList;
import  javafx.event.EventHandler;
import  javafx.event.EventType;

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
// import java.lang.Number;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollBar;
import javafx.scene.Group;
import javafx.scene.Node;

public class InteractivePolygon extends InteractiveShape {
  private ArrayList<Circle> _points = new ArrayList<>();
  private ArrayList<Line> _lines = new ArrayList<>();
  private Polygon _polygon = null;
  private Circle _cursor = null;
  private ObservableList<Anchor> _anchors = null;
  private boolean _collisionDetected = false;
  InteractivePolygon _this = this;


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
    ImageCursor addCursor = new ImageCursor(addIcon,
                                            addIcon.getWidth() / 2,
                                            addIcon.getHeight() /2);
    ImageCursor closeCursor = new ImageCursor(closeIcon,
                                            closeIcon.getWidth() / 2,
                                            closeIcon.getHeight() /2);
    Pane().setCursor(addCursor);

    _cursor.setVisible(false);
    // Pane().setCursor(Cursor.NONE);

    EventHandler<MouseEvent> mouseClickEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseClicked(event)) {
          if (_points.size() > 0 && _cursor.intersects(_points.get(0).getBoundsInParent())) {
            closeForm();
          } else {
            // if (!_collisionDetected)
              addPoint(event);
          }
        }
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_RELEASED, mouseClickEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseClickEvent);

    EventHandler<MouseEvent> mouseMovedEvent = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (onMouseMoved(event)) {
          _collisionDetected = Engine().isObjectUnderCursor(_cursor) ||
            (_lines.size() > 0 ? Engine().isObjectUnderCursor(_lines.get(_lines.size() - 1)) : false);
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
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedEvent);
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);
    Pane().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseMovedEvent);

    // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    // Pane().getChildren().add(_polygon);
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

    _polygon.setFill(Color.DODGERBLUE);
    for (Circle point : _points) {
      _polygon.getPoints().addAll(new Double[] { point.getCenterX(), point.getCenterY() });
    }
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
    // EventHandler<MouseEvent> mouseClickedEvent = EventHandlers().remove(MouseEvent.MOUSE_CLICKED);
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

    return;
  }
  
  private void updateCursor(MouseEvent event) {
    _cursor.setCenterX(event.getX());
    _cursor.setCenterY(event.getY());
  }

  private void updateCurrentLine(MouseEvent event) {
    if (_points.size() == 0)
      return;
  
    // if (_cursor.intersects(_points.get(0).getBoundsInParent())) {
    //   Pane().setCursor(Cursor.HAND);
    // } else if (Pane().get_cursor == Cursor.HAND)
    //   Pane().setCursor(Cursor.DEFAULT);
    
    // Circle lastPoint = _points.get(_points.size() - 1);
    Line activeLine = _lines.get(_lines.size() - 1);
    activeLine.setVisible(true);

    // activeLine.setStartX(lastPoint.getCenterX());
    // activeLine.setStartY(lastPoint.getCenterY());
    activeLine.setEndX(event.getX());
    activeLine.setEndY(event.getY());
  }

  private void select() {
    System.out.println("SHAPE" + this);

    if (Engine().getSelectedShape() != this) {
      Engine().selected(this);
      Pane().getChildren().addAll(_anchors);
    }
  }

  private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    int j = 0;
    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;
      final int idj = j;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
          _lines.get(idj - 1 < 0 ? _lines.size() -1 : idj - 1).setEndX((double) x);
          _lines.get(idj).setStartX((double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
          _lines.get(idj - 1 < 0 ? _lines.size() -1 : idj - 1).setEndY((double) y);
          _lines.get(idj).setStartY((double) y);
        }
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
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          // getScene().setCursor(Cursor.MOVE);
        }
      });
      // setOnMouseReleased(new EventHandler<MouseEvent>() {
      //   @Override public void handle(MouseEvent mouseEvent) {
      //     getScene().setCursor(Cursor.HAND);
      //   }
      // });
      // Circle _circle = this;
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          double newY = mouseEvent.getY() + dragDelta.y;
          updateCursor(mouseEvent);
          // _this._collisionDetected = _this.Engine().isObjectUnderCursor(_this.getCursor());
          Circle tmp = new Circle(newX, newY, 5, Color.TRANSPARENT);
          tmp.setStrokeWidth(2);
          tmp.setStrokeType(StrokeType.OUTSIDE);    
          Pane().getChildren().add(tmp);
          _collisionDetected = _this.Engine().isObjectUnderCursor(tmp);
          // _this._collisionDetected = _this.Engine().isObjectUnderCursor(_this.getShape());
          if (_collisionDetected) {
            Line element = (Line) _this.Engine().getObjectUnderCursor(tmp);
            if (element != null) {
              double x = Pane().getScaleX();
              double y = Pane().getScaleY();
              
              Pane().setScaleX(1.0);
              Pane().setScaleY(1.0);
              Bounds bounds = Engine().scrlPane.getViewportBounds();
              System.out.println("ScrollOffset ---> " + Pane().getParent().getLayoutX());
              System.out.println("ScrollOffset ---> " + Pane().getParent().getLayoutY());
              System.out.println("ScrollOffset ---> " + bounds);
              // System.out.println("ScrollOffset ---> " + Engine().scrlPane.getParent().getParent().getParent().getParent().());
              AnchorPane achPane = (AnchorPane) Engine().scrlPane.getParent().getParent().getParent().getParent().getParent().getParent();
              System.out.println("ScrollOffset ---> " + achPane.getPadding());
              // System.out.println("ScrollOffset ---> " + Engine().scrlPane.getViewportBounds());
              // Point2D scrollOffset = _this.figureScrollOffset(_this.Pane(), _this.Engine().scrlPane);
              Pane().setLayoutX(-Pane().getParent().getLayoutX() - bounds.getMinX());
              Pane().setLayoutY(-Pane().getParent().getLayoutY()- bounds.getMinY() - achPane.getPadding().getTop());
              Shape union = Shape.intersect(tmp, element);
              Pane().setLayoutX(0.0);
              Pane().setLayoutY(0.0);
              Pane().setScaleX(x);
              Pane().setScaleY(y);
              Pane().getChildren().add(union);

              double offsetX = 0.0; // Find why there is an offset
              double offsetY = 0.0; // Find why there is an offset
              setCenterX((union.getBoundsInParent().getMinX() + (union.getBoundsInParent().getMaxX() - union.getBoundsInParent().getMinX()) / 2) + offsetX);
              setCenterY((union.getBoundsInParent().getMinY() + (union.getBoundsInParent().getMaxY() - union.getBoundsInParent().getMinY()) / 2 + offsetY));
              union.setFill(Color.ORCHID);

            }
            setStroke(Color.RED);
          } else {
            setStroke(Color.GOLD);
            if (newX > 0 && newX < getScene().getWidth()) {
              setCenterX(newX);
            }
            // double newY = mouseEvent.getY() + dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight()) {
              setCenterY(newY);
            }
          }
          if (_this.Engine().isObjectUnderCursor(_this.getShape())) {
            _polygon.setFill(Color.RED);
          } else {
            _polygon.setFill(Color.GREEN);
          }
          Pane().getChildren().remove(tmp);
        }
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
    Pane().getChildren().removeAll(_anchors);
    // ArrayList<Node> nodes = new ArrayList<>();
    // for (InteractiveShape shape : Engine().getShapes()) {
    //   nodes.add(shape.getShape());
    //   for (Shape outBound : shape.getOutBoundShapes()) {
    //     nodes.add(outBound);
    //   }
    //   if (shape == this && Engine().getSelectedShape() == null) {
    //     for (Shape extShapes : shape.getExtShapes()) {
    //       nodes.add(extShapes);
    //     }
    //   }
    // }
    // -----------
    // Group group = new Group(nodes);
    // Engine().getPane().getChildren().remove(Engine().getGroup());
    // Engine().getPane().getChildren().add(group);
    // Engine().setGroup(group);
  }
  private class Delta { double x, y; }

  //TODO remove these
  public Shape getShape() {
    return _polygon;
  }

  public Circle getCursor() {
    return _cursor;
  }


  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    // System.out.println("_ " + extraWidth);
    double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    // System.out.println("_ " + hScrollProportion);
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    // System.out.println("_ " + scrollXOffset);
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    // System.out.println("_ " + extraHeight);
    double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    // System.out.println("_ " + vScrollProportion);
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    // System.out.println("_ " + scrollYOffset);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
      double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
  }

}