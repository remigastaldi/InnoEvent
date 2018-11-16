/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 16th November 2018
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
import com.inno.ui.innoengine.shape.IrregularSection;
import com.inno.ui.innoengine.shape.RectangularSection;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import  javafx.scene.shape.Shape;
import javafx.geometry.Point2D;


public class InnoEngine extends Engine {
  // TEST
  String statut = "";

  public InnoEngine(View view, Pane pane) {
    super(pane);

    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);
  }

  public void createIrregularSection() {
    deselect();
    IrregularSection innoPoly = new IrregularSection(this, getPane());
    innoPoly.start();
  }

  public void createRectangularSection() {
    deselect();
    RectangularSection innoPoly = new RectangularSection(this, getPane());
    innoPoly.start();
  }

  public void createScene(double[] pos) {
    // IrregularSection
  }
}