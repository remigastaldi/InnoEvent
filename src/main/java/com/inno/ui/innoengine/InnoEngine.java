/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

import java.util.Collection;

import com.inno.app.Core;
import com.inno.app.room.ImmutableRoom;
import com.inno.app.room.ImmutableScene;
import com.inno.app.room.ImmutableSection;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.View;
import com.inno.ui.engine.Engine;
import com.inno.ui.innoengine.shape.InnoPolygon;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class InnoEngine extends Engine {
  View _view = null;

  public InnoEngine(View view, StackPane stackPane) {
    super(stackPane, 100, 100);
    _view = view;

    ImmutableRoom roomData = Core.get().getImmutableRoom();

    getPane().setPrefSize(roomData.getWidth(), roomData.getHeight());
    
    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);

    Collection<? extends ImmutableSittingSection> sections = roomData.getImmutableSittingSections().values();
    for (ImmutableSittingSection section : sections) {
       createInteractiveRectangle(section.getIdSection(), section.getPositions()[0], section.getPositions()[1],
                                  section.getPositions()[2] - section.getPositions()[0], section.getPositions()[7] - section.getPositions()[3],
                                  section.getRotation(),
                                  Color.ROYALBLUE);
      }

    ImmutableScene dto = Core.get().getImmutableRoom().getImmutableScene();

    createInteractiveRectangle("-1", dto.getPositions()[0], dto.getPositions()[1], dto.getWidth(), dto.getHeight(), dto.getRotation(), Color.ROYALBLUE);
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

  public View getView() {
    return _view;
  }
}