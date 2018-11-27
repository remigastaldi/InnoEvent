/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 27th November 2018
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
import com.sun.scenario.effect.Effect;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public abstract class InteractiveShape<T extends Shape> {
  private Engine  _engine = null;
  private Pane _pane = null;
  private HashMap<EventType<MouseEvent>, EventHandler<MouseEvent>> _eventHandlers = new HashMap<>();
  private ArrayList<Shape> _outBoundShapes = new ArrayList<>();
  private ArrayList<Shape> _selectShapes = new ArrayList<>();
  private ArrayList<Shape> _additionalShapes = new ArrayList<>();
  private String _id = null;
  private Rotate _currentRotation = null; 
  // TODO: pass to private
  protected T _shape = null;
  protected Group _group;
  protected ObservableList<CircleAnchor> _anchors = null;


  InteractiveShape(Engine engine, Pane pane) {
    _engine = engine;
    _pane = pane;
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
  public void onShapeChanged() {};

  public abstract void start();

  // TODO: Generic points
  public abstract void setPoints(double[] points);
  public abstract double[] getPoints();
  public abstract double[] getPointsInParent();

  protected void addOutboundShape(Shape shape) {
    _outBoundShapes.add(shape);
  }

  public ArrayList<Shape> getOutBoundShapes() {
    return _outBoundShapes;
  }

  public ArrayList<Shape> getSelectShapes() {
    return _selectShapes;
  }

  public ArrayList<Shape> getAdditionalShapes() {
    return _additionalShapes;
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
    borderGlow.setColor(Color.GOLD);
    borderGlow.setWidth(depth);
    borderGlow.setHeight(depth);
    _group.setEffect(borderGlow);
  }

  public void disableGlow() {
    _group.setEffect(null);
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

  public void addAdditionalShape(Shape shape) {
    _additionalShapes.add(shape);
  }
  
  public void setColor(Color color) {
    _shape.setFill(color.deriveColor(1, 1, 0.8, 0.85));
    for (Shape shape : _outBoundShapes) {
      shape.setStroke(color);
    }
  }

  public void setRotationAngle(double rotation) {
    setRotation(new Rotate(rotation, _currentRotation.getPivotX(), _currentRotation.getPivotY()));
  }
  
  public void setRotation(double angle, double x, double y) {
    setRotation(new Rotate(angle, x, y));
  }

  public void setRotation(Rotate rotate) {
    if (_currentRotation != null)
      _group.getTransforms().remove(_currentRotation);
    _currentRotation = rotate;
    _group.getTransforms().add(_currentRotation);
  }

  public Rotate getRotation() {
    return _currentRotation;
  }

  public void completeShape() {
    for (CircleAnchor anchor : _anchors) {
      _selectShapes.add(anchor.getShape());
    }
    ArrayList<Node> nodes = new ArrayList<>();
    nodes.add(_shape);
    for (Shape outBound : _outBoundShapes) {
      Pane().getChildren().remove(outBound);
      nodes.add(outBound);
    }
    for (Shape selectShape : _selectShapes) {
      Pane().getChildren().remove(selectShape);
      nodes.add(selectShape);
    }
    for (Shape additionalShape : _additionalShapes) {
      nodes.add(additionalShape);
    }
    _group = new Group(nodes);
    Pane().getChildren().add(_group);
    _engine.addInteractiveShape(this);
    
    for (CircleAnchor anchor : _anchors) {
      anchor.setInteractiveShape(this);
    }

    onFormComplete();
  }

  public void refreshGroup() {
    ObservableList<Transform> transforms = _group.getTransforms();
    // double rotate = _group.getRotate();

    ArrayList<Node> nodes = new ArrayList<>();
    nodes.add(_shape);
    for (Shape outBound : _outBoundShapes) {
      nodes.add(outBound);
    }
    for (Shape selectShape : _selectShapes) {
      nodes.add(selectShape);
    }
    for (Shape additionalShape : _additionalShapes) {
      nodes.add(additionalShape);
    }
    Pane().getChildren().remove(_group);
    _group = new Group(nodes);
    Pane().getChildren().add(_group);
    _group.getTransforms().addAll(transforms);

    _group.setOnMousePressed(mouseEvent -> {
      select();
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group) (_group)).getTranslateX();
      orgTranslateY = ((Group) (_group)).getTranslateY();
    });

  }

  double orgSceneX, orgSceneY;
  double orgTranslateX, orgTranslateY;

  public void enableSelection() {
    // EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
    //   public void handle(MouseEvent event) {
    //   }
    // };
    // EventHandlers().put(MouseEvent.MOUSE_CLICKED, mouseClick);
    // _shape.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);

    EventHandler<MouseEvent> mouseDragged = mouseEvent -> {
      if (onMouseOnDragDetected(mouseEvent)) {
        // TODO: Magnetism between anchors and lines

        // Rectangle shape = new Rectangle(_shape.getX(), _shape.getY(),
        //                                 _shape.getWidth(), _shape.getHeight());
        // shape.setFill(Color.TRANSPARENT);
        // shape.setStroke(Color.WHITE);
        // ObservableList<Transform> effect = _group.getTransforms();
        // if (effect != null && effect.size() > 0)
        //   shape.getTransforms().add(effect.get(0));

        Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
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
        //   _shape.setFill(Color.RED);
        // Pane().getChildren().remove(shape);
        onShapeChanged();
      }
    };
    EventHandlers().put(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    _shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);

    _group.setOnMousePressed(mouseEvent -> {
      select();
      Point2D p = Pane().sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

      orgSceneX = p.getX();
      orgSceneY = p.getY();
      orgTranslateX = ((Group) (_group)).getTranslateX();
      orgTranslateY = ((Group) (_group)).getTranslateY();
    });
  }

  public void  select() {
    if (Engine().getSelectedShape() != this) {
      onSelected();
      _shape.toFront();
      for (Shape selectShape : getSelectShapes()) {
        selectShape.toFront();
        selectShape.setVisible(true);
      }
      for (Shape additionalShape : _additionalShapes) {
        additionalShape.toFront();
      }
      enableGlow();
      Engine().selected(this);
      System.out.println("SHAPE" + this + " SELECTED");
    }
  }

  public void   deselect() {
    for (Shape selectShape : getSelectShapes()) {
      selectShape.setVisible(false);
    }

    disableGlow();
    System.out.println("DESELECTED");
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
}