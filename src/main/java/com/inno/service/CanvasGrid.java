/*
 * File Created: Saturday, 13th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasGrid {
  private double  _spacing = 5.0;
  private Canvas  _canvas = null;
  private GraphicsContext _gc = null;
  private Color _color = Color.WHITE;
  private double _width = 1.0;

  public CanvasGrid(Canvas canvas) {
    _canvas = canvas;
    _gc = canvas.getGraphicsContext2D();
    updateGrid();
  }

  public void zoom(double factor) {
    updateGrid();
  }

  public void setGridSpacing(double spacing) {
    _spacing = spacing;
    updateGrid();
  }

  public void setGridColor(Color color) {
    _color = color;
    updateGrid();
  }

  public void setGridLinesWidth(double width) {
    updateGrid();
  }

  private void updateGrid() {
    double xNeeded = _canvas.getWidth() / _spacing;
    double yNeeded = _canvas.getHeight() / _spacing;
  
    for (double i = 1; i < xNeeded; i += _spacing) {
      double pos = i * _spacing;

      _gc.setStroke(_color);
      _gc.setLineWidth(_width);
      _gc.strokeLine(pos, 0, pos, _canvas.getHeight());
    }
    
    for (double i = 1; i < yNeeded; i += _spacing) {
      double pos = i * _spacing;

      _gc.setStroke(_color);
      _gc.setLineWidth(_width);
      _gc.strokeLine(0, pos, _canvas.getWidth(), pos);
    }
  }
}