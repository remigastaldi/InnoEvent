/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: HUBERT Léo
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
    System.out.println("++++++++++++++++++++++");
    if (getWidth() < Engine().meterToPixel(_xVitalSpace))
      setWidth(Engine().meterToPixel(_xVitalSpace));
    if (getHeight() < Engine().meterToPixel(_yVitalSpace))
      setHeight(Engine().meterToPixel(_yVitalSpace));

    if (!_grabbed) {
      double[] pos = getPositions();
      double[] newPos = new double[] { pos[0] - getWidth(), pos[1] - getHeight(), pos[2] - getWidth(),
          pos[3] - getHeight(), pos[4] - getWidth(), pos[5] - getHeight(), pos[6] - getWidth(), pos[7] - getHeight() };
      _sectionData = Core.get().createSittingSection(localToParent(newPos), 0);
    } else
      _sectionData = Core.get().createSittingSection(getPositionsInParent(), 0);
    loadFromData();
    InnoEngine engine = (InnoEngine) Engine();
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  @Override
  public void onShapeChanged() {
    Core.get().updateSectionPositions(getID(), getPositionsInParent());
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

  public void loadData() {
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());
  }

  private void loadFromData(Group group) {
    setID(_sectionData.getIdSection());

    if (group != null)
      setPositions(parentToLocal(_sectionData.getPositions()));
    else
      setPositions(_sectionData.getPositions());
    setRotation(new Rotate(_sectionData.getRotation(), getX() + getWidth(), getY() + getHeight()));
  }

  private void loadFromData() {
    loadFromData(null);
  }

  public double[] getPositions() {
    double[] pos = { getX(), getY(), getMaxXProperty().get(), getY(), getMaxXProperty().get(), getMaxYProperty().get(),
        getX(), getMaxYProperty().get() };
    return pos;
  }

  public double[] getPositionsInParent() {
    return localToParent(getPositions());
  }

  public double[] localToParent(double[] pos) {
    Point2D lu = _group.localToParent(pos[0], pos[1]);
    Point2D ru = _group.localToParent(pos[2], pos[3]);
    Point2D rd = _group.localToParent(pos[4], pos[5]);
    Point2D ld = _group.localToParent(pos[6], pos[7]);
    // System.out.println(lu);
    return new double[] { lu.getX(), lu.getY(), ru.getX(), ru.getY(), rd.getX(), rd.getY(), ld.getX(), ld.getY() };
  }

  public double[] parentToLocal(double[] pos) {
    Point2D lu = _group.parentToLocal(pos[0], pos[1]);
    Point2D ru = _group.parentToLocal(pos[2], pos[3]);
    Point2D rd = _group.parentToLocal(pos[4], pos[5]);
    Point2D ld = _group.parentToLocal(pos[6], pos[7]);
    // System.out.println(lu);
    return new double[] { lu.getX(), lu.getY(), ru.getX(), ru.getY(), rd.getX(), rd.getY(), ld.getX(), ld.getY() };
  }

  // @Override
  // public void setRotation(double rotation) {
  // _rotation = rotation;
  // _group.getTransforms().clear();
  // // _group.getTransforms().add(new Rotate(rotation, getX() + getWidth(),
  // getY() + getHeight()));
  // // Point2D pos = new Point2D(getX(), getY());
  // // pos = _group.localToParent(pos.getX(), pos.getY());
  // // _group.getTransforms().add(new Rotate(rotation, pos.getX(), pos.getY()));
  // // _group.getTransforms().add(new Rotate(rotation, getMaxXProperty().get(),
  // getMaxYProperty().get()));
  // }
}