/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: MAREL Maud
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
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import  javafx.scene.input.MouseEvent;

public class InnoPolygon extends InteractivePolygon {
  private ImmutableStandingSection _standingSectionData = null;
  private ImmutableSittingSection _sittingSectionData = null;
  private boolean mouseReleased = false;

  public InnoPolygon(InnoEngine engine, Pane pane) {
    super(engine, pane);
  }

  public InnoPolygon(InnoEngine engine, Pane pane, String id, double[] pos, Rotate rotation, Color color) {
    super(engine, pane, pos, rotation, color);

    setID(id);
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
    mouseReleased= false;
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent event) {
    mouseReleased= true;
    return true;
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (mouseReleased) {
      _sittingSectionData = Core.get().createSittingSection(localToParent(getPoints()), 0);
      loadFromData();
    }
    return true;
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    return true;
  }

  @Override
  public void onShapeChanged() {
    Core.get().updateSectionPositions(getID(), getPointsInParent());
    Core.get().setSectionRotation(getID(), getRotation().getAngle());
    loadFromData(_group);
  }

  @Override
  public boolean onSelected() {
    InnoEngine engine = (InnoEngine) Engine();
    engine.getView().setSidebarFromFxmlFileName("sidebar_room.fxml", this);
    return true;
  }


  public void loadDomainData() {
    _sittingSectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());
  }

  private void loadFromData(Group group) {
    setID(_sittingSectionData.getIdSection());

    if (group != null)
      setPoints(parentToLocal(_sittingSectionData.getPositions()));
    else
      setPoints(_sittingSectionData.getPositions());
      Point2D center = Engine().getCenterOfPoints(getPoints());
    setRotation(new Rotate(_sittingSectionData.getRotation(),center.getX(), center.getY()));
  }

  private void loadFromData() {
    loadFromData(null);
  }

  public void changeSectionType() {
    //TODO Sitting to standing anv vis versa
  }
}