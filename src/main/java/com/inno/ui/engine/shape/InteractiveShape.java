/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 17th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.engine.shape;

import java.util.ArrayList;
import java.util.HashMap;

import com.inno.ui.engine.CircleAnchor;
import com.inno.ui.engine.CustomCursor;
import com.inno.ui.engine.Engine;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public abstract class InteractiveShape<T extends Shape> {
  private Engine _engine = null;
  private Pane _pane = null;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();
  private ArrayList<Shape> _outBoundShapes = new ArrayList<>();
  private ArrayList<Shape> _selectShapes = new ArrayList<>();
  private ArrayList<Shape> _additionalShapes = new ArrayList<>();
  private String _id = null;
  private Rotate _currentRotation = null;
  private Rectangle _boundsRect = null;
  // TODO: pass to private
  protected T _shape = null;
  protected Group _group;
  protected ObservableList<CircleAnchor> _anchors = null;


  InteractiveShape(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
    _group = new Group();

  }

  // Callback
  public boolean onMouseEntered(MouseEvent event) { return true; }
  public boolean onMouseClicked(MouseEvent event) { return true; }
  public boolean onMouseExited(MouseEvent event)  { return true; }
  public boolean onMouseMoved(MouseEvent event)   { return true; }
  public boolean onMousePressed(MouseEvent event)  { return true; }
  public boolean onMouseReleased(MouseEvent event) { return true; }
  public boolean onMouseOnDragDetected(MouseEvent event) { return true; }
  public boolean onMouseOnDragDropped(MouseEvent event) { return true; }
  public boolean onFormComplete() { return true; }
  public boolean onSelected() { return true; }
  public boolean onDestroy() { return true; }
  
  public boolean onShapeMoved() { return true; }
  public boolean onShapeReleased() { return true; }
  
  public boolean onAnchorPressed() { return true; }
  public boolean onAnchorDragged() { return true; }
  public boolean onAnchorReleased() { return true; }

  public abstract void start();

  // TODO: Generic points
  public abstract void setPoints(double[] points);
  public abstract void updatePoints(double[] points);
  public abstract double[] getPoints();
  public abstract double[] getPointsInParent();
  public abstract double[] getNoRotatedParentPos();
  public abstract double[] noRotatedParentPointsToRotated(double pos[]);


  // REMOVE THIS ASDYASUDYASIUDYASIDYTASIUDTIUYWQTEIUYWQTEIYTQWIEUTQWIEYTQWIEUYT
  public void updateRowsFromData(boolean toParent){};

  protected void addOutboundShape(Shape shape) {
    _outBoundShapes.add(shape);
    _group.getChildren().add(shape);
  }

  public ArrayList<Shape> getOutBoundShapes() {
    return _outBoundShapes;
  }

  public void removeOutBoundShape(Shape shape) {
    _outBoundShapes.remove(shape);
    _group.getChildren().remove(shape);
  }

  public void clearOutBoundShape() {
    _group.getChildren().removeAll(_outBoundShapes);
  }

  public void addSelectShape(Shape shape) {
    _selectShapes.add(shape);
    _group.getChildren().add(shape);
  }

  public ArrayList<Shape> getSelectShapes() {
    return _selectShapes;
  }

  public void removeSelectShape(Shape shape) {
    _selectShapes.remove(shape);
    _group.getChildren().remove(shape);
  }

  public void clearSelectShape() {
    _group.getChildren().removeAll(_selectShapes);
  }

  public void addAdditionalShape(Shape shape) {
    _additionalShapes.add(shape);
    _group.getChildren().add(shape);
  }

  public ArrayList<Shape> getAdditionalShapes() {
    return _additionalShapes;
  }

  public void removeAdditionalShape(Shape shape) {
    _additionalShapes.remove(shape);
    _group.getChildren().remove(shape);
  }

  public void clearAdditionalShape() {
    _group.getChildren().removeAll(_additionalShapes);
  }

  public void setShape(T shape) {
    if (_shape != null)
      _group.getChildren().remove(shape);

    _shape = shape;
    _group.getChildren().add(shape);
  }

  protected Pane Pane() {
    return _pane;
  }

  protected HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> EventHandlers() {
    return _eventHandlers;
  }

  protected Engine Engine() {
    return _engine;
  }

  public CustomCursor Cursor() {
    return _engine.getCursor();
  }

  public void enableGlow() {
    int depth = 10;
    DropShadow borderGlow = new DropShadow();
    // borderGlow.setBlurType(BlurType.GAUSSIAN);
    borderGlow.setOffsetY(0f);
    borderGlow.setOffsetX(0f);
    borderGlow.setColor(Color.GOLD.deriveColor(1, 1, 0.8, 0.7));
    borderGlow.setWidth(depth);
    borderGlow.setHeight(depth);
    _shape.setEffect(borderGlow);
  }

  public void disableGlow() {
    _shape.setEffect(null);
  }

  public Group getGroup() {
    return _group;
  }

  public void setID(String id) {
    _id = id;
  }

  public String getID() {
    return _id;
  }

  public void setColor(Color color) {
    _shape.setFill(color.deriveColor(1, 1, 0.8, 0.85));
    for (Shape shape : _outBoundShapes) {
      shape.setStroke(color);
    }
  }

  public void setRotationAngle(double rotation) {
    System.out.println("Rotation angle: " + rotation);
    _currentRotation.setAngle(rotation);
  }
  
  public void setRotation(double angle, double x, double y) {
    System.out.println("====================+++++++++++++==================");
    setRotation(new Rotate(angle, x, y));
  }

  public void setRotation(Rotate rotate) {
    System.out.println("####################################################");
    if (_currentRotation != null)
      _group.getTransforms().remove(_currentRotation);
    _currentRotation = rotate;
    _group.getTransforms().add(_currentRotation);
  }

  public Rotate getRotation() {
    return _currentRotation;
  }

  // public void relocateGroup() {
  //   ArrayList<Node> nodes = new ArrayList<>();
  //   nodes.add(_shape);
  //   for (Shape outBound : _outBoundShapes) {
  //     nodes.add(outBound);
  //   }
  //   for (Shape selectShape : _selectShapes) {
  //     nodes.add(selectShape);
  //   }
  //   for (Shape additionalShape : _additionalShapes) {
  //     nodes.add(additionalShape);
  //   }
  //   _group = new Group(nodes);
  //   Pane().getChildren().add(_group);

  //   _group.setOnMousePressed(mouseEvent -> {
  //     select();
  //     Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

  //     orgSceneX = p.getX();
  //     orgSceneY = p.getY();
  //     orgTranslateX = ((Group) (_group)).getTranslateX();
  //     orgTranslateY = ((Group) (_group)).getTranslateY();
  //   });
  // }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;
  double boundsTranslateX, boundsTranslateY;
  Rotate rectBoundsRotation = null;

  public void enableSelection() {
    EventHandler<MouseEvent> mouseDragged = mouseEvent -> {
      if (onMouseOnDragDetected(mouseEvent)) {
        // TODO: Magnetism between anchors and lines

        Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        double offsetX = p.getX() - orgSceneX;
        double offsetY = p.getY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        double testTranslateX = boundsTranslateX + offsetX;
        double testTranslateY = boundsTranslateY + offsetY;

        rectBoundsRotation.setAngle(getRotation().getAngle());

        // System.out.println(_boundsRect.getBoundsInParent().getMinX());
        // System.out.println(Engine().getBoard().getWidth() - (_boundsRect.getBoundsInParent().getMinX() + _boundsRect.getWidth()));
        // System.out.println(Engine().getBoard(). getHeight() - (_boundsRect.getBoundsInParent().getMinY() + _boundsRect.getHeight()));
        double boardWidth = Engine().getBoard().getWidth();
        double boardHeight = Engine().getBoard().getHeight();
        Bounds rectBounds = _boundsRect.getBoundsInParent();
        Bounds groupBounds = _group.getBoundsInParent();
        if (rectBounds.getMaxX() >= boardWidth || rectBounds.getMinX() <= Engine().getBoard().getX()) {
          if (rectBounds.getMinX() <= Engine().getBoard().getX())
            _boundsRect.setTranslateX(testTranslateX - rectBounds.getMinX());
          else if (rectBounds.getMaxX() >= Engine().getBoard().getHeight()) {
            _boundsRect.setTranslateX(testTranslateX - (rectBounds.getMaxX() - boardWidth));
          } else
            _boundsRect.setTranslateX(testTranslateX);

          if (Engine().isOutOfBoard(_boundsRect) 
            && groupBounds.getMinY() - 2 > Engine().getBoard().getY()
            && groupBounds.getMaxY() + 2 < Engine().getBoard().getHeight()) {
              _group.setTranslateY(newTranslateY);
          }
        } else if (rectBounds.getMaxY() >= boardHeight || rectBounds.getMinY() <= Engine().getBoard().getY()) {
          if (rectBounds.getMinY() <= Engine().getBoard().getY())
            _boundsRect.setTranslateY(testTranslateY - rectBounds.getMinY());
          else if (rectBounds.getMaxY() >= Engine().getBoard().getHeight()) {
            _boundsRect.setTranslateY(testTranslateY - (rectBounds.getMaxY() - boardHeight));
          } else
            _boundsRect.setTranslateY(testTranslateY);

          if (Engine().isOutOfBoard(_boundsRect)
            && groupBounds.getMinX() - 2 > Engine().getBoard().getX()
            && groupBounds.getMaxX() + 2 < Engine().getBoard().getWidth()) {
              _group.setTranslateX(newTranslateX);
          }
        } else {
          _boundsRect.setTranslateX(testTranslateX);
          _boundsRect.setTranslateY(testTranslateY);
          if (!Engine().isOutOfBoard(_boundsRect)) {
            _group.setTranslateX(newTranslateX);
            _group.setTranslateY(newTranslateY);
          }
        }

        // if (!Engine().isObjectUnderCursor(shape)) {
        //   // _group.setTranslateX(newTranslateX);
        //   // _group.setTranslateY(newTranslateY);
        //   _shape.setFill(Color.ROYALBLUE);
        // } else
        //   _shape.setFill(Color.RED);
        // Pane().getChildren().remove(shape);
        onShapeMoved();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    _group.setOnMousePressed(mouseEvent -> {
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group) (_group)).getTranslateX();
      orgTranslateY = ((Group) (_group)).getTranslateY();

      Bounds bounds = _shape.getBoundsInParent();
      Point2D bMin = _group.localToParent(bounds.getMinX(), bounds.getMinY());
      // Point2D bMax = _group.localToParent(bounds.getMaxX(), bounds.getMaxY());
      _boundsRect = new Rectangle(bMin.getX(), bMin.getY(), bounds.getMaxX() - bounds.getMinX(), bounds.getMaxY() - bounds.getMinY());
      // _boundsRect.setFill(Color.TRANSPARENT);
      // _boundsRect.setStroke(Color.GOLD);
      // _boundsRect.setStrokeWidth(0.3);
      rectBoundsRotation = new Rotate(getRotation().getAngle(), _boundsRect.getX(), _boundsRect.getY());
      _boundsRect.getTransforms().add(rectBoundsRotation);
      // Pane().getChildren().add(_boundsRect);

      boundsTranslateX = ((Rectangle) (_boundsRect)).getTranslateX();
      boundsTranslateY = ((Rectangle) (_boundsRect)).getTranslateY();
      select();
    });

    _group.setOnMouseReleased(mouseEvent -> {
      onShapeReleased();
      rectBoundsRotation = null;
      Pane().getChildren().remove(_boundsRect);
    });
  }

  public void  select() {
    if (Engine().getSelectedShape() != this) {
      if (!onSelected())
        return;
      _shape.toFront();
      for (Shape additionalShape : _additionalShapes) {
        additionalShape.toFront();
      }
      for (Shape selectShape : getSelectShapes()) {
        selectShape.toFront();
        selectShape.setVisible(true);
      }
      enableGlow();
      Engine().selected(this);
      System.out.println("SHAPE " + this + " SELECTED");
    }
  }

  public void   deselect() {
    for (Shape selectShape : getSelectShapes()) {
      selectShape.setVisible(false);
    }

    disableGlow();
    System.out.println(this + " DESELECTED");
  }

  public Shape  getShape() {
    return _shape;
  }

  public void   destroy() {
    _engine.deselect();
    onDestroy();
    Pane().getChildren().remove(_group);
  }

  public double[] localToParent(double[] pos) {
    double[] newPos = new double[pos.length];

    for (int i = 0; i < pos.length; i += 2) {
      Point2D val = _group.localToParent(pos[i], pos[i + 1]);
      newPos[i] = val.getX();
      newPos[i + 1] = val.getY();
    }
    return newPos;
  }

  public double[] parentToLocal(double[] pos) {
    double[] newPos = new double[pos.length];

    for (int i = 0; i < pos.length; i += 2) {
      Point2D val = _group.parentToLocal(pos[i], pos[i + 1]);
      newPos[i] = val.getX();
      newPos[i + 1] = val.getY();
    }
    return newPos;
  }

  public Color getColor() {
    return (Color) _shape.getFill();
  }
}