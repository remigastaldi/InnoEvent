/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 19th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine.shape;

import  com.inno.ui.innoengine.InnoEngine;
import  com.inno.ui.engine.shape.InteractiveRectangle;

import  javafx.scene.layout.Pane;
import  javafx.scene.input.MouseEvent;

public class InnoRectangle extends InteractiveRectangle {
  private double _xVitalSpace = 1.5;
  private double _yVitalSpace = 1.5;

  public InnoRectangle(InnoEngine engine, Pane pane) {
    super(engine, pane);
  }

  @Override
  public boolean onMouseClicked(MouseEvent event) {
    InnoEngine engine = (InnoEngine)Engine();
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);
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
    InnoEngine engine = (InnoEngine)Engine();
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);
    return true;
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (getWidth() < Engine().meterToPixel(_xVitalSpace));
      setWidth(Engine().meterToPixel(_xVitalSpace));
    if (getHeight() < Engine().meterToPixel(_yVitalSpace))
      setHeight(Engine().meterToPixel(_yVitalSpace));
    return true;
  }
  
  public void setColumnNumber(int columns) {
    System.out.println("SET " + Engine().meterToPixel(_xVitalSpace * columns));
    // getWidthProperty().set(Engine().meterToPixel(_xVitalSpace * columns));
    setWidth(Engine().meterToPixel(_xVitalSpace * columns));
  }

  public void setRowNumber(int rows) {
    // getHeightProperty().set(Engine().meterToPixel(_yVitalSpace * rows));
    setHeight(Engine().meterToPixel(_yVitalSpace * rows));
  }

  public int getColumnNumber() {
    return (int) (getWidth() / Engine().meterToPixel(_xVitalSpace));
  }

  public int getRowNumber() {
    return (int) (getHeight() / Engine().meterToPixel(_yVitalSpace));
  }
}