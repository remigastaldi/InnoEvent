/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 3rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;

import java.util.ArrayList;

import com.inno.ui.engine.shape.InteractivePolygon;
import com.inno.ui.engine.shape.InteractiveShape;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import  javafx.event.EventType;
import  javafx.event.EventHandler;
import  javafx.scene.input.MouseEvent;
import  javafx.scene.shape.Rectangle;
import  javafx.scene.shape.Shape;
import  javafx.scene.shape.Line;
import  javafx.scene.Group;

import javafx.scene.control.ScrollPane;

public class Engine {
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private ArrayList<InteractiveShape> _shapes = new ArrayList<>();
  private Grid _grid = null;
  private Rectangle _board = null;
  private boolean _collisions = true;
  private InteractivePolygon _selectedShape = null;

  // private Group _group = null;

  public Engine(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();

    
// _pane.setScaleX(2.0);
// _pane.setScaleY(2.0);

    _board = new Rectangle(0, 0, _pane.getWidth(), _pane.getHeight());
    _board.setStrokeWidth(0.0);
    _board.setFill(Color.TRANSPARENT);

    EventHandler<MouseEvent> mouseClick = event -> {
      System.out.println("PANE");
      if (_selectedShape != null)
        deselect();
    };
    _board.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
    _nodes.add(_board);
  }

  public void setBackgroundColor(Color color) {
    String val = Integer.toHexString(color.hashCode()).toUpperCase();

    _pane.setStyle("-fx-background-color: #" + val);
  }

  public void activateGrid(boolean val) {
    if (val) {
      if (_grid == null) {
        _grid = new Grid(_pane);
        _grid.setColor(Color.valueOf("#777A81"));
        _grid.setLinesWidth(0.4);
        _grid.activate();
      }
    } else {
      _grid.disable();
      _grid = null;
    }
  }

  public void createInteractivePolygon() {
    _shapes.add(new InteractivePolygon(this, _pane));
  }

  public void addInteractiveShape(InteractiveShape intShape) {
    _shapes.add(intShape);
  }

  public void selected(InteractivePolygon selected) {
    // _shapeIgnorePane = true;

    if (_selectedShape != null) {
      _selectedShape.deselect();
    }
    _selectedShape = selected;
  }

  public InteractiveShape getSelectedShape() {
    return _selectedShape;
  }

  public void deselect() {
    _selectedShape.deselect();
    _selectedShape = null;
  }

  public Pane getPane() {
    return _pane;
  }

  public boolean isObjectUnderCursor(Shape cursor) {
    for (InteractiveShape element : _shapes) {
      if (element == _selectedShape)
        continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          System.out.println(" ++++++++++ Line collision ++++++++++");
          return true;
        }
      }
      // System.out.println(cursor.getBoundsInParent());
      Shape intersect = Shape.intersect(cursor, element.getShape());
      if (intersect.getBoundsInParent().getWidth() != -1) {
        // System.out.println(intersect.getBoundsInParent().getMaxX() + " : " +
        //   intersect.getBoundsInParent().getMaxX());
        // System.out.println("collision");
        return true;
      }
    }
    // for (Shape shape : _grid.getLines()) {
    //   System.out.println(" ========================= ");

    //   Shape intersect = Shape.intersect(cursor, shape);
    //   if (intersect.getBoundsInParent().getWidth() != -1) {
    //     System.out.println(" ++++++++++ Line collision ++++++++++");
    //     return true;
    //   }
    // }
  return false;
  }

  public Shape getObjectUnderCursor(Shape cursor) {
    for (InteractiveShape element : _shapes) {
      if (element == _selectedShape)
        continue;
      for (Shape shape : element.getOutBoundShapes()) {
        Shape intersect = Shape.intersect(cursor, shape);
        if (intersect.getBoundsInParent().getWidth() != -1) {
          System.out.println(" ++++++++++ Stroke ++++++++++");        
          return shape;
        }
      }
      // System.out.println(cursor.getBoundsInLocal());
      // Shape intersect = Shape.intersect(cursor, element.getShape());
      // if (intersect.getBoundsInLocal().getWidth() != -1) {
        //   System.out.println(intersect.getBoundsInParent().getMinX() + " : " +
        //     intersect.getBoundsInLocal().getMaxX());
        //   System.out.println("collision");
        //   // collisionDetected = true;
        //   return element.getShape();
        // }
      }
      // for (Shape shape : _grid.getLines()) {
      //   Shape intersect = Shape.intersect(cursor, shape);
      //   if (intersect.getBoundsInParent().getWidth() != -1) {
      //     System.out.println(" ++++++++++ Stroke ++++++++++");        
      //     return shape;
      //   }
      // }
    return null;
  }
  // public boolean isOtherShapeUnderCursor(Shape cursor) {
  //   for (InteractiveShape element : _shapes) {
  //     if (element == _selectedShape)
  //     Shape intersect = Shape.intersect(cursor, element.getShape());
  //     if (intersect.getBoundsInLocal().getWidth() != -1) {
  //       System.out.println("collision");
  //       // collisionDetected = true;
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  public Rectangle getBoard() {
    return _board;
  }


  //TODO remove this
  public ArrayList<InteractiveShape> getShapes() {
    return _shapes;
  };

  public ArrayList<Shape> getAllShapes() {
    ArrayList<Shape> shapes = new ArrayList<>();

    for (InteractiveShape element : _shapes) {
      shapes.add(element.getShape());
      for (Shape shape : element.getOutBoundShapes()) {
        shapes.add(shape);
      }
      for (Shape shape : element.getExtShapes()) {
        shapes.add(shape);
    }
  }

    return shapes;
  }

  public ScrollPane scrlPane = null;

  // public Group getGroup() {
  //   return _group;
  // }

  // public void setGroup(Group group) {
  //   _group = group;
  // }
}