/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.innoengine.shape;

import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.ui.engine.shape.InteractiveRectangle;
import com.inno.ui.innoengine.InnoEngine;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class InnoRectangle extends InteractiveRectangle {
  private double _xVitalSpace = 0.0;
  private double _yVitalSpace = 0.0;
  private ImmutableSittingSection _sectionData = null;

  private boolean _grabbed = false;

  public InnoRectangle(InnoEngine engine, Pane pane) {
    super(engine, pane);

    _xVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth();
    _yVitalSpace = Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight();
  }

  public InnoRectangle(InnoEngine engine, Pane pane, String id, double x, double y, double width, double height,
      Rotate rotation, Color color) {
    super(engine, pane, x, y, width, height, rotation, color);

    setID(id);
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

    if (!_grabbed) {
      double[] pos = getPoints();
      double[] newPos = new double[] { pos[0] - getWidth(), pos[1] - getHeight(), pos[2] - getWidth(),
          pos[3] - getHeight(), pos[4] - getWidth(), pos[5] - getHeight(), pos[6] - getWidth(), pos[7] - getHeight() };
      _sectionData = Core.get().createSittingSection(localToParent(newPos), 0);
    } else
      _sectionData = Core.get().createSittingSection(getPointsInParent(), 0);
    loadFromData();
    InnoEngine engine = (InnoEngine) Engine();
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  @Override
  public void onShapeChanged() {
    Core.get().updateSectionPositions(getID(), getPointsInParent());
    Core.get().setSectionRotation(getID(), getRotation().getAngle());
    loadFromData(_group);
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    _grabbed = true;
    return true;
  }

  @Override
  public boolean onFormComplete() {
    // if (getWidth() < Engine().meterToPixel(_xVitalSpace))
    // setWidth(Engine().meterToPixel(_xVitalSpace));
    // if (getHeight() < Engine().meterToPixel(_yVitalSpace))
    // setHeight(Engine().meterToPixel(_yVitalSpace));

    // double[] pos = {getX(), getY(),
    // getMaxXProperty().get(), getY(),
    // getMaxXProperty().get(), getMaxYProperty().get(),
    // getX(), getMaxYProperty().get() };
    // _sectionData = Core.get().createSittingSection(0, pos, 0);
    return true;
  }

  @Override
  public boolean onSelected() {
    InnoEngine engine = (InnoEngine) Engine();
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
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

  public void loadDomainData() {
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());
  }

  private void loadFromData(Group group) {
    setID(_sectionData.getIdSection());

    if (group != null)
      setPoints(parentToLocal(_sectionData.getPositions()));
    else
      setPoints(_sectionData.getPositions());
    setRotation(new Rotate(_sectionData.getRotation(), getX() + getWidth(), getY() + getHeight()));
  }

  private void loadFromData() {
    loadFromData(null);
  }
}