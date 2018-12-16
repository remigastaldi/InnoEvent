/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 15th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

import java.util.Collection;
import java.util.HashMap;

import com.inno.app.Core;
import com.inno.app.room.ImmutableRoom;
import com.inno.app.room.ImmutableScene;
import com.inno.app.room.ImmutableSection;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.ImmutableStandingSection;
import com.inno.ui.View;
import com.inno.ui.engine.Engine;
import com.inno.ui.engine.shape.InteractiveShape;
import com.inno.ui.innoengine.shape.InnoPolygon;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;


public class InnoEngine extends Engine {
  private View _view = null;
  ImmutableSection _buffSection = null;
  private HashMap<String, InnoRectangle> _rectangles = new HashMap<>();
  private HashMap<String, InnoPolygon> _polygons = new HashMap<>();
  

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
  	Collection<? extends ImmutableSittingSection> sittingSections = roomData.getImmutableSittingSections().values();
    for (ImmutableSittingSection section : sittingSections) {
      if (section.isRectangle()) {
        System.out.println("Load rectangular sitting section");
        createRectangularSection(section.getId());
      } else {
        System.out.println("Load irregular sitting section");
        createIrregularSection(section.getId(), false);
      }
    }
  	Collection<? extends ImmutableStandingSection> standingSection = roomData.getImmutableStandingSections().values();
    for (ImmutableStandingSection section : standingSection) {
      System.out.println("Load irregular standing section");
      createIrregularSection(section.getId(), true);
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
      meterToPixel(dto.getWidth()), meterToPixel(dto.getHeight()), new Rotate(dto.getRotation(), pos[0] + meterToPixel(dto.getWidth() / 2), pos[1] + meterToPixel(dto.getHeight() / 2)), Color.ROYALBLUE) {
        @Override
        public boolean onAnchorDragged() {
          Core.get().setScenePositions(pixelToMeter(getNoRotatedParentPos()));
          Core.get().setSceneWidth(pixelToMeter(this.getWidth()));
          Core.get().setSceneHeight(pixelToMeter(this.getHeight()));
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

        @Override
        public boolean onShapeMoved() {
          Core.get().setScenePositions(pixelToMeter(getNoRotatedParentPos()));
          updateRectangleSectionsOrientation(true);
          updatePolygonRowsOrientation(true);
          return true;
        }

        @Override
        public boolean onShapeReleased() {
          updateRectangleSectionsOrientation(false);
          updatePolygonRowsOrientation(false);
          return true;
        }
    };
    Color color = Color.BLUEVIOLET;
    shape.setColor(color);
    shape.getShape().setFill(color.deriveColor(1, 1, 0.8, 0.85));
    deselect();
    addInteractiveShape(shape);
  }

  protected void updatePolygonRowsOrientation(boolean toParent) {
    for (InnoPolygon shape : _polygons.values()) {
      Core.get().updateSectionPositions(shape.getID(), pixelToMeter(shape.getNoRotatedParentPos()), false);
      shape.updateFromData(toParent);
    }
  }

  public void updateRectangleSectionsOrientation(boolean toParent) {
    for (InnoRectangle shape : _rectangles.values()) {
      Core.get().updateSectionPositions(shape.getID(), pixelToMeter(shape.getNoRotatedParentPos()), true);
      shape.updateFromData(toParent);
    }
  }

  public InnoPolygon createIrregularSection() {
    deselect();
    InnoPolygon shape = new InnoPolygon(this, getPane());
    shape.start();
    addInteractiveShape(shape);
    return shape;
  }

  public InnoPolygon createIrregularSection(String id, double[] pos, Rotate rotation, Color color) {
    deselect();
    InnoPolygon shape = new InnoPolygon(this, getPane(), id, pos, rotation, color);
    addInteractiveShape(shape);
    deselect();
    _polygons.put(shape.getID(), shape);
    return shape;
  }

  public InnoPolygon createIrregularSection(String id, boolean isStanding) {
    deselect();
    InnoPolygon shape = new InnoPolygon(this, getPane(), id, isStanding);
    addInteractiveShape(shape);
    _polygons.put(shape.getID(), shape);
    return shape;
  }

  public InnoRectangle createRectangularSection() {
    deselect();
    InnoRectangle shape = new InnoRectangle(this, getPane());
    shape.start();
    addInteractiveShape(shape);
    return shape;
  }

  public InnoRectangle createRectangularSection(double x, double y, double width, double height, Rotate rotation, Color color) {
    deselect();
    InnoRectangle shape = new InnoRectangle(this, getPane(), x, y, width, height, rotation, color);
    addInteractiveShape(shape);
    _rectangles.put(shape.getID(), shape);
    return shape;
  }

  public InnoRectangle createRectangularSection(String id) {
    deselect();
    InnoRectangle shape = new InnoRectangle(this, getPane(), id);
    addInteractiveShape(shape);
    _rectangles.put(shape.getID(), shape);
    return shape;
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

  public void changeRoomWidth(double width) {
    double pixWidth = meterToPixel(width);
    setBoardWidth(pixWidth);
    Core.get().setRoomWidth(pixWidth);
  }

  public void changeRoomHeight(double height) {
    double pixHeight = meterToPixel(height);
    setBoardHeight(pixHeight);
    Core.get().setRoomHeight(pixHeight);
    
  }

  public void copySelectedSectionsToDomainBuffer() {
    Core core = Core.get();

    core.copySectionToBuffer(getSelectedShape().getID());
  }

  public void pastBufferToEngine() {
    Core core = Core.get();

    ImmutableSection section = core.createSectionFromBuffer();

    if (section == null)
      return;

    if (section.isRectangle()) {
      createRectangularSection(section.getId()).select();
    } else if (section.isStanding()) {
      createIrregularSection(section.getId(), true).select();
    } else {
      createIrregularSection(section.getId(), false).select();
  }
  core.copySectionToBuffer(getSelectedShape().getID());
}

  /**
   * Update all sections vital which have old one
   * @param width old width
   * @param height old height
   */
  public void updateSectionsVitalSpaceFromData(double width, double height) {
    for (InteractiveShape<? extends Shape> shape : getShapes()) {
      shape.updateRowsFromData(false);
    }
  }

  public void addRectangle(InnoRectangle rectangle) {
    _rectangles.put(rectangle.getID(), rectangle);
  }

  public void deleteRectangle(String id) {
    _rectangles.remove(id);
  }

  public void addPolygon(InnoPolygon polygon) {
    _polygons.put(polygon.getID(), polygon);
  }

  public void deletePolygon(String id) {
    _polygons.remove(id);
  }

  public void deleteSittingSection(String id) {
    InnoPolygon polygon = _polygons.get(id);
    if (polygon != null) {
      polygon.destroy();
      _polygons.remove(id);
    }
    else {
      InnoRectangle rectangle = _rectangles.get(id);
      if (rectangle != null) {
        rectangle.destroy();
        _rectangles.remove(id);
      }
    }
  }

  public void updateSectionFromData(String id) {
    InnoPolygon polygon = _polygons.get(id);
    if (polygon != null) {
      polygon.updateFromData(false);
    }
    else {
      InnoRectangle rectangle = _rectangles.get(id);
      ImmutableSittingSection section = Core.get().getImmutableRoom().getImmutableSittingSections().get(rectangle.getID());
      
      if (rectangle != null) {
        double[] pos = rectangle.parentToLocal(meterToPixel(section.getPositions()));

        rectangle.setPoints(pos);
        // rectangle.setRotation(new Rotate(section.getRotation(), meterToPixel(section.getPositions())[0], meterToPixel(section.getPositions())[1]));
        // rectangle.getRotation().setPivotX(pos[0]);
        // rectangle.getRotation().setPivotY(pos[1]);
    
        rectangle.updateRowsFromData(false);
        rectangle.setRotationAngle(section.getRotation());
    
        // rectangle.updateFromData(false);
      }
    }
  }

  public void updateAllSectionsFromData() {
    for (InnoRectangle rectangle : _rectangles.values()) {
      rectangle.updateFromData(false);
    }
    for (InnoPolygon polygon : _polygons.values()) {
      polygon.updateFromData(false);
    }
  }

  public void deleteSection(String id) {
    InnoPolygon polygon = _polygons.get(id);
    if (polygon != null) {
      polygon.destroy();
      _polygons.remove(id);
    }
    else {
      InnoRectangle rectangle = _rectangles.get(id);
      if (rectangle != null) {
        rectangle.destroy();
        _rectangles.remove(id);
      }
    } 
  }
}