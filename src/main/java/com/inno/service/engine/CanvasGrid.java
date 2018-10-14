/*
 * File Created: Saturday, 13th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class CanvasGrid {
  private double  _spacing = 5.0;
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private Color _color = Color.WHITE;
  private double _width = 1.0;

  public CanvasGrid(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();
  }

  public void zoom(double factor) {
  }

  public void setGridSpacing(double spacing) {
    _spacing = spacing;
  }

  public void setGridColor(Color color) {
    _color = color;
  }

  public void setGridLinesWidth(double width) {
  }

  private void activateGrid() {
    double xNeeded = _pane.getWidth() / _spacing;
    double yNeeded = _pane.getHeight() / _spacing;
  
    // for (double i = 1; i < xNeeded; i += _spacing) {
    //   double pos = i * _spacing;

    //   _gc.setStroke(_color);
    //   _gc.setLineWidth(_width);
    //   _gc.strokeLine(pos, 0, pos, _pane.getHeight());
    // }
    
    // for (double i = 1; i < yNeeded; i += _spacing) {
    //   double pos = i * _spacing;

    //   _gc.setStroke(_color);
    //   _gc.setLineWidth(_width);
    //   _gc.strokeLine(0, pos, _pane.getWidth(), pos);
    // }
  }
}