/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine.shape;

import com.inno.ui.engine.Engine;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public class InteractiveRectangle extends InteractiveShape {

  public InteractiveRectangle(Engine engine, Pane pane) {
    super(engine, pane);
  }

  @Override
  public void start() {

  }

  @Override
  public void deselect() {

  }

  @Override
  public Shape getShape() {
    return null;
}

}