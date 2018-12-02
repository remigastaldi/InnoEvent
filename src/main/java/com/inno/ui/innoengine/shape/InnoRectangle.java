/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 2nd December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.innoengine.shape;

import java.util.ArrayList;

import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingRow;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.engine.shape.InteractiveRectangle;
import com.inno.ui.innoengine.InnoEngine;
import com.inno.ui.innoengine.InnoRow;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class InnoRectangle extends InteractiveRectangle {
  private double _xVitalSpace = 0.0;
  private double _yVitalSpace = 0.0;
  private ImmutableSittingSection _sectionData = null;
  private InnoRow[] _rows = null;
  private boolean _mousePressed = false;

  public InnoRectangle(InnoEngine engine, Pane pane) {
    super(engine, pane);

    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth());
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight());
  }

  public InnoRectangle(InnoEngine engine, Pane pane, double x, double y, double width, double height,
      Rotate rotation, Color color) {
    super(engine, pane, x, y, width, height, rotation, color);

    setID("");
    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth());
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight());
  }

  public InnoRectangle(InnoEngine engine, Pane pane, String id) {
    super(engine, pane);

    setID(id);
    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth());
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight());
    loadFromData();
    deselect();
  }

  @Override
  public boolean onMouseClicked(MouseEvent event) {
    return true;
  };

  @Override
  public boolean onMouseEntered(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onMouseExited(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onMouseMoved(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onMousePressed(MouseEvent event) {
    _mousePressed = true;
    return true;
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    // updateFromData();
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent event) {
    _mousePressed = false;

    if (getWidth() < ((InnoEngine)Engine()).meterToPixel(_xVitalSpace))
      setWidth(((InnoEngine)Engine()).meterToPixel(_xVitalSpace));
    if (getHeight() < ((InnoEngine)Engine()).meterToPixel(_yVitalSpace))
      setHeight(((InnoEngine)Engine()).meterToPixel(_yVitalSpace));

    _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, true);
    setID(_sectionData.getIdSection());
    updateFromData();

    InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  @Override
  public boolean onShapeMoved() {
    if (_sectionData == null)
      return true;
    System.out.println("################### MOVED ######################");
    // double[] test = ((InnoEngine)Engine()).pixelToMeter(doubledoubledoublegetPointsInParent());
    // for (int i = 0; i < test.length; i+=2) {
    //   System.out.println(test[i] + " " + test[i + 1 ]);
    // }
    // Core.get().setSectionRotation(getID(), getRotation() != null ? getRotation().getAngle() : 0.0);
    
    // double pos[] = getPoints();
    // for (int i =0; i < pos.length; i+=2) {
      //   System.out.println(pos[i] + " ; " + pos[i + 1]);
      // }
    // getGroup().getTransforms().clear();
Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);
    // getGroup().getTransforms().addAll(transforms);
    // loadFromData();
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    setPoints(pos);
setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));
    // updateFromData();
    return true;
  }

  @Override
  public boolean onShapeResized() {
    if (_sectionData == null)
      return true;

    System.out.println("################### RESIZED ######################");

    getGroup().getTransforms().clear();
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);

    updateFromData();
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (_mousePressed == true) {

      double[] pos = getPointsInParent();
      for (int i = 0; i < pos.length; i += 2) {
        System.out.println("X: " + pos[i] + " Y: " + pos[i + 1]);
      } 

      // _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, true);
      // setID(_sectionData.getIdSection());
      // updateFromData();
    }

    return true;
  }

  @Override
  public boolean onSelected() {
    InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  public void setColumnNumber(int columns) {
    setWidth(_xVitalSpace * columns);
  }

  public void setRowNumber(int rows) {
    setHeight(_yVitalSpace * rows);
  }

  public int getColumnNumber() {
    return (int) (getWidth() / _xVitalSpace);
  }

  public int getRowNumber() {
    return (int) (getHeight() / _yVitalSpace);
  }

  public ImmutableSittingSection getSectionData() {
    return _sectionData;
  }

  public void setVitalSpace(double width, double height) {
    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(width);
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(height);

    // System.outprintln()
    Core.get().setSittingSectionVitalSpace(_sectionData.getIdSection(), width, height);
  }

  public void setHeightFromMetter(double height) {
    this.setHeight(((InnoEngine)Engine()).meterToPixel(height));
  }

  public double getHeightToMetter() {
    return ((InnoEngine)Engine()).pixelToMeter(this.getHeight());
  }

  public void setWidthFromMetter(double width) {
    this.setWidth(((InnoEngine)Engine()).meterToPixel(width));
  }

  public double getWidthToMetter() {
    return ((InnoEngine)Engine()).pixelToMeter(this.getWidth());
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    return true;
  }

  public void loadFromData() {
    // System.out.println("################### CREATED ######################");
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());

    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    closeForm(pos[0], pos[1], Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));
    
    updateFromData();
  } 

  private void updateFromData() {
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    
    if (_rows != null) {
      for (int i = 0; i < _rows.length; ++i) {
        _rows[i].destroy();
      }
    }

    ArrayList<? extends ImmutableSittingRow> rows =  _sectionData.getImmutableSittingRows();
    _rows = new InnoRow[rows.size()];

    int i = 0;
    for (ImmutableSittingRow row : rows) {
      InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
      _rows[i] = new InnoRow(engine, this, _sectionData, row);
      ++i;
    }

    setPoints((pos));
    // getGroup().getTransforms().clear();
    
    setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));
  }
}