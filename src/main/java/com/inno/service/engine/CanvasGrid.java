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

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class CanvasGrid {
  private Pane  _pane = null;
  private ObservableList<Node> _nodes = null;
  private ArrayList<Line> _lines = new ArrayList<>();
  private double  _xSpacing = 5.0;
  private double  _ySpacing = 5.0;
  private Color _color = Color.WHITE;
  private double _width = 1.0;

  public CanvasGrid(Pane pane) {
    _pane = pane;
    _nodes = pane.getChildren();
  }

  public void zoom(double factor) {
  }

  public void setXSpacing(double spacing) {
    _xSpacing = spacing;
  }

  public void setYSpacing(double spacing) {
    _ySpacing = spacing;
  }

  public void setColor(Color color) {
    _color = color;
  }

  public void setLinesWidth(double width) {
    _width = width;
  }

  public void activate() {
    double xNeeded = _pane.getPrefWidth() / _xSpacing;
    double yNeeded = _pane.getPrefHeight() / _ySpacing;
  
    for (double i = 1; i < xNeeded; i += _xSpacing) {
      double pos = i * _xSpacing;
      Line line = new Line(pos, 0, pos, _pane.getPrefHeight());

      line.setStroke(_color);
      line.setStrokeWidth(_width);
      _lines.add(line);
      _nodes.add(line);
    }
    
    for (double i = 1; i < yNeeded; i += _ySpacing) {
      double pos = i * _ySpacing;
      Line line = new Line(0, pos, _pane.getPrefWidth(), pos);

      line.setStroke(_color);
      line.setStrokeWidth(_width);
      _lines.add(line);
      _nodes.add(line);
    }
  }

  public void disable() {
    for (Line line : _lines) {
      _nodes.remove(line);
    }
  }
}