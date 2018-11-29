/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 29th November 2018
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
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.View;
import com.inno.ui.engine.Engine;
import com.inno.ui.innoengine.shape.InnoPolygon;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;


public class InnoEngine extends Engine {
  View _view = null;

  public InnoEngine(View view, StackPane stackPane) {
    super(stackPane, 100, 100);
    _view = view;

    ImmutableRoom roomData = Core.get().getImmutableRoom();

    getPane().setPrefSize(meterToPixel(roomData.getWidth()), meterToPixel(roomData.getHeight()));
    
    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);


    loadSections(roomData);
    loadScene();
  }

  private void loadSections(ImmutableRoom roomData) {
  	Collection<? extends ImmutableSittingSection> sections = roomData.getImmutableSittingSections().values();
    for (ImmutableSittingSection section : sections) {
      double[] pos = meterToPixel(section.getPositions());
      Point2D center = getCenterOfPoints(pos);
      if (section.isRectangle()) {
        System.out.println("Load rectangular section");
        double width = Math.hypot(pos[0] - pos[2], pos[1] - pos[3]);
        double height = Math.hypot(pos[6] - pos[0], pos[7] - pos[1]);
        createRectangularSection(section.getIdSection());
      } else {
        System.out.println("Load irregular section");
        createIrregularSection(section.getIdSection(), pos,
          new Rotate(section.getRotation(), center.getX(), center.getY()), Color.LIGHTSKYBLUE);
        }
    }
  }

  public void loadScene() {
    ImmutableScene dto = Core.get().getImmutableRoom().getImmutableScene();

    if (dto == null) {
      System.out.println("No scene data");
      return;
    }
    
    double[] pos = meterToPixel(dto.getPositions());
    InnoRectangle shape = new InnoRectangle(this, getPane(), pos[0], pos[1],
      meterToPixel(dto.getWidth()), meterToPixel(dto.getHeight()), new Rotate(dto.getRotation(), pos[0] + meterToPixel(dto.getWidth()), pos[1] + meterToPixel(dto.getHeight())), Color.ROYALBLUE) {
        @Override
        public boolean onShapeResized() {
          Core.get().setScenePositions(pixelToMeter(getPointsInParent()));
          Core.get().setSceneWidth(pixelToMeter(this.getWidth()));
          Core.get().setSceneHeight(pixelToMeter(this.getHeight()));
          // Core.get().setSceneRotation(this.getRotation().getAngle());

          return true;
        }

        @Override
        public boolean onSelected() {
          _view.setSidebarFromFxmlFileName("sidebar_scene.fxml", this);
          return true;
        }

        @Override
        public boolean onDestroy() {
          Core.get().deleteScene();
          _view.setSidebarFromFxmlFileName("sidebar_room.fxml", this);
          return true;
        }
    };
    Color color = Color.BLUEVIOLET;
    shape.setColor(Color.BLUEVIOLET);
    shape.getShape().setFill(color.deriveColor(1, 1, 0.8, 0.85));
    deselect();
  }

  public void createIrregularSection() {
    deselect();
    InnoPolygon section = new InnoPolygon(this, getPane());
    section.start();
  }

  public void createIrregularSection(String id, double[] pos, Rotate rotation, Color color) {
    deselect();
    InnoPolygon section = new InnoPolygon(this, getPane(), id, pos, rotation, color);
    section.loadDomainData();
    addInteractiveShape(section);
    deselect();
  }

  public void createRectangularSection() {
    deselect();
    InnoRectangle innoPoly = new InnoRectangle(this, getPane());
    innoPoly.start();
  }

  public void createRectangularSection(double x, double y, double width, double height, Rotate rotation, Color color) {
    InnoRectangle section = new InnoRectangle(this, getPane(), x, y, width, height, rotation, color);
    addInteractiveShape(section);
    deselect();
  }

  public void createRectangularSection(String id) {
    InnoRectangle section = new InnoRectangle(this, getPane(), id);
    section.loadDomainData();
    addInteractiveShape(section);
    deselect();
  }

  public View getView() {
    return _view;
  }


  public double pixelToMeter(double pixel) {
    return pixel / getScale();
  }

  public double[] pixelToMeter(double[] pos) {
    double[] newPos = new double[pos.length];

    for (int i = 0; i < pos.length; ++i) {
      newPos[i] = pixelToMeter(pos[i]);
    }

    return newPos;
  }

  public double meterToPixel(double meter) {
    return meter * getScale();
  }

  public double[] meterToPixel(double[] pos) {
    double[] newPos = new double[pos.length];

    for (int i = 0; i < pos.length; ++i) {
      newPos[i] = meterToPixel(pos[i]);
    }

    return newPos;
  }

  @Override
  public boolean onBoardSelected(MouseEvent event) {
    _view.setSidebarFromFxmlFileName("sidebar_room.fxml", this);
    return true;
  }
}