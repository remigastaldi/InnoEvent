/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine.shape;

import  com.inno.app.Core;
import  com.inno.app.room.ImmutableSittingSection;
import  com.inno.ui.engine.shape.InteractiveRectangle;
import  com.inno.ui.innoengine.InnoEngine;

import  javafx.scene.input.MouseEvent;
import  javafx.scene.layout.Pane;
import  javafx.scene.paint.Color;

public class InnoRectangle extends InteractiveRectangle {
  private double _xVitalSpace = 0.0;
  private double _yVitalSpace = 0.0;
  private ImmutableSittingSection _sectionData = null;

  public InnoRectangle(InnoEngine engine, Pane pane) {
    super(engine, pane);

    _xVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth();
    _yVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight();
  }

  public InnoRectangle(InnoEngine engine, Pane pane, String id, double x, double y, double width, double height, double rotation, Color color) {
    super(engine, pane, id, x, y, width, height, rotation, color);

    _xVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth();
    _yVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight();
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
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent event) {
    if (getWidth() < Engine().meterToPixel(_xVitalSpace))
      setWidth(Engine().meterToPixel(_xVitalSpace));
    if (getHeight() < Engine().meterToPixel(_yVitalSpace))
      setHeight(Engine().meterToPixel(_yVitalSpace));

    _sectionData = Core.get().createSittingSection(0, getPositions(), 0);

    loadFromData();
    InnoEngine engine = (InnoEngine)Engine();
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);

    return true;
  }

  private void loadFromData() {
    setID(_sectionData.getIdSection());
    setPositions(_sectionData.getPositions());
    setRotation(_sectionData.getRotation());
  }

  @Override
  public void onShapeChanged() {
    Core.get().updateSectionPositions(getID(), getPositions());
    loadFromData();
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onFormComplete() {
    // if (getWidth() < Engine().meterToPixel(_xVitalSpace))
    //   setWidth(Engine().meterToPixel(_xVitalSpace));
    // if (getHeight() < Engine().meterToPixel(_yVitalSpace))
    //   setHeight(Engine().meterToPixel(_yVitalSpace));

    // double[] pos = {getX(), getY(),
    //                 getMaxXProperty().get(), getY(),
    //                 getMaxXProperty().get(), getMaxYProperty().get(),
    //                 getX(), getMaxYProperty().get() };
    // _sectionData = Core.get().createSittingSection(0, pos, 0);
    return true;
  }
  
  public void setColumnNumber(int columns) {
    setWidth(Engine().meterToPixel(_xVitalSpace * columns));
  }

  public void setRowNumber(int rows) {
    setHeight(Engine().meterToPixel(_yVitalSpace * rows));
  }

  public int getColumnNumber() {
    return (int) (getWidth() / Engine().meterToPixel(_xVitalSpace));
  }

  public int getRowNumber() {
    return (int) (getHeight() / Engine().meterToPixel(_yVitalSpace));
  }

  public ImmutableSittingSection getSectionData() {
    return _sectionData;
  }

  public void setVitalSpace(double width, double height) {
    _xVitalSpace = width;
    _yVitalSpace = height;

    // System.outprintln()
    Core.get().setSittingSectionVitalSpace(_sectionData.getIdSection(), width, height);
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    return true;
  }

  public double[] getPositions() {
    double[] pos = {getX(), getY(),
      getMaxXProperty().get(), getY(),
      getMaxXProperty().get(), getMaxYProperty().get(),
      getX(), getMaxYProperty().get() };
      return pos;
  }
}