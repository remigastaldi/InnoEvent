/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.engine;

import java.util.ArrayList;

import com.inno.service.engine.shape.InteractivePolygon;
import com.inno.service.engine.shape.InteractiveShape;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Engine {
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private ArrayList<InteractiveShape> _shapes = new ArrayList<>();
  private CanvasGrid _grid = null;

  public Engine(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();
  }

  public void setBackgroundColor(Color color) {
    String val = Integer.toHexString(color.hashCode()).toUpperCase();

    _pane.setStyle("-fx-background-color: #" + val);
  }

  public void activateGrid(boolean val) {
    if (val) {
      if (_grid == null) {
        _grid = new CanvasGrid(_pane);
        _grid.setColor(Color.valueOf("#777A81"));
        _grid.setLinesWidth(0.5);
        _grid.activate();
      }
    } else {
      _grid.disable();
      _grid = null;
    }
  }

  public void createPolygon() {
    _shapes.add(new InteractivePolygon(this, _pane));
  }

  public void addInteractiveShape(InteractiveShape intShape) {
    _shapes.add(intShape);
  }

  public Pane getPane() {
    return _pane;
  }

}