/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 9th December 2018
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
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent event) {
    _mousePressed = false;

    if (getWidth() < (_xVitalSpace))
      setWidth (_xVitalSpace);
    if (getHeight() < (_yVitalSpace))
      setHeight (_yVitalSpace);

    _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), 0, true);
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
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);

    updatePositionFromData();
    return true;
  }

  @Override
  public boolean onAnchorDragged() {
    if (_sectionData == null)
      return true;

    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updateRowsFromData(true);
    return true;
  }

  @Override
  public boolean onAnchorReleased() {
    double[] parent = getPointsInParent();
    
    getRotation().setAngle(0);
    double [] pos = parentToLocal(parent);
 
    getGroup().getTransforms().clear();
    setPoints(pos);

    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);

    setPoints(pos);
    double rotation = _sectionData != null ? _sectionData.getRotation() : 0;
    setRotation(new Rotate(rotation, pos[0], pos[1]));
    updateRowsFromData(false);
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (_mousePressed == true) {
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
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updateFromData();    
  }

  public void setRowNumber(int rows) {
    setHeight(_yVitalSpace * rows);
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updateFromData();    
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
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());

    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    
    closeForm(pos[0], pos[1], new Rotate(), Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));
    updateFromData();
  } 

  private void updateFromData() {
    updatePositionFromData();
    updateRowsFromData(false);
  }

  private void updatePositionFromData() {
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));

    setPoints(pos);
    getRotation().setPivotX(pos[0]);
    getRotation().setPivotY(pos[1]);
    getRotation().setAngle(_sectionData.getRotation());
  }

  private void updateRowsFromData(boolean toParent) {
    if (_rows != null) {
      for (int i = 0; i < _rows.length; ++i) {
        _rows[i].destroy();
      }
    }

    ArrayList<? extends ImmutableSittingRow> rows =  _sectionData.getImmutableSittingRows();
    _rows = new InnoRow[rows.size()];

    int i = 0;
    for (ImmutableSittingRow row : rows) {
      InnoEngine engine = (InnoEngine) ((InnoEngine) Engine());
      _rows[i] = new InnoRow(engine, this, _sectionData, row, toParent);
      ++i;
    }
  }
}