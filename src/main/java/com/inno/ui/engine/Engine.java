/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
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

public class Engine {
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private ArrayList<InteractiveShape> _shapes = new ArrayList<>();
  private Grid _grid = null;
  
  
  private boolean _shapeIgnorePane = false;
  private InteractivePolygon _selectedShape = null;

  public Engine(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();

    EventHandler<MouseEvent> mouseClick = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        if (_selectedShape != null)  
          deselect();
        // setSelected(true);
      }
    };
    _pane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
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
    _shapeIgnorePane = true;

    if (_selectedShape != null) {
      _selectedShape.deselect();
    }
    _selectedShape = selected;
  }

  public void deselect() {
    if (_shapeIgnorePane) {
      _shapeIgnorePane = false;
      return;
    }

    System.out.println("PANE");
    _selectedShape.deselect();
    _selectedShape = null;
  }

  public Pane getPane() {
    return _pane;
  }
}