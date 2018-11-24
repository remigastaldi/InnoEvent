/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 23rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

import com.inno.app.Core;
import com.inno.app.room.ImmutableRoom;
import com.inno.app.room.ImmutableScene;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.View;
import com.inno.ui.engine.Engine;
import com.inno.ui.innoengine.shape.InnoPolygon;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class InnoEngine extends Engine {
  // TEST
  String statut = "";
  View _view = null;

  public InnoEngine(View view, StackPane stackPane) {
    super(stackPane, 100, 100);
    _view = view;

    ImmutableRoom roomData = Core.get().getImmutableRoom();
    ImmutableScene sceneData = roomData.getImmutableScene();

    // Rectangle scene = new Rectangle(sceneData.getPositions()[0], sceneData.getPositions()[1],
    //                                 sceneData.getWidth(), sceneData.getHeight());
    // scene.setFill(Color.CHARTREUSE);
    // scene.setOpacity(0.8);
    // _pane.getChildren().add(scene);

    getPane().setPrefSize(roomData.getWidth(), roomData.getHeight());

    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);

    for (ImmutableSittingSection section : roomData.getImmutableSittingSections().values()) {

    }

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