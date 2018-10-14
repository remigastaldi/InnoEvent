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

import  com.inno.service.engine.CanvasGrid;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class Engine {
  private CanvasGrid _grid = null;
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;

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
        _grid.setGridColor(Color.valueOf("#777A81"));
        _grid.setGridLinesWidth(0.5);
        _grid.activateGrid();
      }
    } else {
      _grid = null;
    }
  }
}