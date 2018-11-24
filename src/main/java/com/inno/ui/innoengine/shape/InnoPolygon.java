/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine.shape;

import  com.inno.ui.innoengine.InnoEngine;
import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.ImmutableStandingSection;
import  com.inno.ui.engine.shape.InteractivePolygon;

import  javafx.scene.layout.Pane;
import  javafx.scene.input.MouseEvent;

public class InnoPolygon extends InteractivePolygon {
  private ImmutableStandingSection standingSectionData = null;
  private ImmutableSittingSection sittingSectionData = null;

  public InnoPolygon(InnoEngine engine, Pane pane) {
    super(engine, pane);
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
    return true;
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onFormComplete() {
    // sittingSectionData = Core.get().cr
    return true;
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    return true;
  }

  public void changeSectionType() {
    //TODO Sitting to standing anv vis versa
  }
}