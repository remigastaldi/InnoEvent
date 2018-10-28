/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 28th October 2018
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

public class Engine {
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private ArrayList<InteractiveShape> _shapes = new ArrayList<>();
  private Grid _grid = null;
  private Rectangle _board = null;
  private boolean _collisions = true;
  private InteractivePolygon _selectedShape = null;

  public Engine(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();


    _board = new Rectangle(0, 0, _pane.getPrefWidth(), _pane.getPrefHeight());
    _board.setStrokeWidth(0.0);
    _board.setFill(Color.TRANSPARENT);



    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        System.out.println("PANE");
        if (_selectedShape != null)  
          deselect();
      }
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
      System.out.println(cursor.getBoundsInLocal());
      Shape intersect = Shape.intersect(cursor, element.getShape());
      if (intersect.getBoundsInLocal().getWidth() != -1) {
        System.out.println("collision");
        // collisionDetected = true;
        return true;
      }
    }
    return false;
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
}