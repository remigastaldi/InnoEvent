/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine.shape;

import  com.inno.ui.engine.Engine;
import  com.inno.ui.engine.shape.InteractivePolygon;

import  javafx.scene.layout.Pane;
import  javafx.scene.input.MouseEvent;

public class InnoPolygon extends InteractivePolygon {
  public InnoPolygon(Engine engine, Pane pane) {
    super(engine, pane);
  }

  @Override
  public boolean onMouseClicked(MouseEvent event) {
    System.out.println("+++++++++++++++++++++++");
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
    return true;
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    return true;
  }

}