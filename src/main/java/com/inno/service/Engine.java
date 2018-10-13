/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service;

import  com.inno.service.CanvasGrid;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Engine {
  private Canvas _canvas = null;
  private GraphicsContext _gc = null;
  private CanvasGrid _grid = null;

  public Engine(Canvas canvas) {
    _canvas = canvas;
    _gc = canvas.getGraphicsContext2D();
  }

  public void setBackground(Color color) {
    _gc.setFill(color);
    _gc.fillRect(0, 0, _canvas.getWidth(), _canvas.getHeight());
  }

  public void activateGrid(boolean val) {
    if (val) {
      if (_grid == null) {
        _grid = new CanvasGrid(_canvas);
        _grid.setGridColor(Color.valueOf("#777A81"));
      }
    } else {
      _grid = null;
    }
  }
  // public void set
}