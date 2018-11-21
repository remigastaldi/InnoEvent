/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 21st November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

import com.inno.app.Core;
import com.inno.ui.View;
// import com.inno.ui.engine.room.StandingSection;
import com.inno.ui.engine.Engine;
import com.inno.ui.innoengine.shape.InnoPolygon;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import  javafx.scene.shape.Shape;
import javafx.geometry.Point2D;


public class InnoEngine extends Engine {
  // TEST
  String statut = "";
  View _view = null;

  public InnoEngine(View view, StackPane stackPane) {
    super(stackPane);

    _view = view;
    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);
  }

  public void createIrregularSection() {
    deselect();
    InnoPolygon innoPoly = new InnoPolygon(this, getPane());
    innoPoly.start();
  }

  public void createRectangularSection() {
    deselect();
    InnoRectangle innoPoly = new InnoRectangle(this, getPane());
    innoPoly.start();
  }

  public void createScene(double[] pos) {
    // InnoPolygon
  }

  public View getView() {
    return _view;
  }
}