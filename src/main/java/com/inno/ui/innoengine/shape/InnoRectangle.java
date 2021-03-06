/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
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
  private Rectangle _ghost = null;
  private Rotate _ghostRotation = null;

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

    double[] pos =  getNoRotatedParentPos();
    pos[0] = pos[4];
    pos[1] = pos[5];
    pos[2] = pos[0] + getWidth();
    pos[3] = pos[1];
    pos[4] = pos[2];
    pos[5] = pos[1] + getHeight();
    pos[6] = pos[0];
    pos[7] = pos[5];

    _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(pos), 0, true);
    setID(_sectionData.getId());

    updateFromData(false);
    select();
    checkShapeCollision();
    ((InnoEngine)Engine()).addRectangle(this);
    return true;
  }

  @Override
  public boolean onShapeMoved() {
    if (_sectionData == null)
      return true;
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updatePositionsFromData();
    checkShapeCollision();

    return true;
  }

  @Override
  public boolean onAnchorPressed() {
    if (_sectionData == null)
      return true;

    _ghost = new Rectangle(getX(), getY(), getWidth(), getHeight());
    _ghost.setFill(Color.TRANSPARENT);
    _ghost.setStroke(Color.GOLD);
    _ghost.setStrokeWidth(1d);

    _ghostRotation = new Rotate(getRotation().getAngle(), getX(), getY());
    _ghost.getTransforms().add(_ghostRotation);

    updateRectangleGhost(getNoRotatedParentPos());

    Pane().getChildren().add(_ghost);
    return true;
  }

  @Override
  public boolean onAnchorDragged() {
    if (_sectionData == null)
      return true;

    double[] pos = getNoRotatedParentPos();
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(pos), true);
    updateRowsFromData(true);
    updateRectangleGhost(pos);

    return true;
  }

  @Override
  public boolean onAnchorReleased() {
    if (_sectionData == null)
      return true;

    Pane().getChildren().remove(_ghost);
    _ghost = null;
    _ghostRotation = null;

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
  public boolean onShapeReleased() {
    onGroupSelected();
    return true;
  }

  @Override
  public boolean onGroupSelected() {
    if (getID() == null)
      return false;
    InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  public void setColumnNumber(int columns) {
    setWidth((_xVitalSpace * columns) + 1);
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updateFromData(false);    
  }

  public void setRowNumber(int rows) {
    setHeight((_yVitalSpace * rows) + 1);
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), true);
    updateFromData(false);    
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

    Core.get().setSittingSectionVitalSpace(_sectionData.getId(), width, height);
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
    ((InnoEngine)Engine()).deleteRectangle(getID());
    return true;
  }

  public void loadFromData() {
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());

    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    
    closeForm(pos[0], pos[1], new Rotate(), Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));
    updateFromData(false);
  } 

  public void updateFromData(boolean toParent) {
    updatePositionsFromData();
    updateRowsFromData(toParent);
    checkShapeCollision();
  }

  public void updatePositionsFromData() {
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));

    setPoints(pos);
    Rotate rotation = getRotation();
    rotation.setPivotX(pos[0]);
    rotation.setPivotY(pos[1]);
    rotation.setAngle(_sectionData.getRotation());
  }

  public void updateRowsFromData(boolean toParent) {
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

  private void updateRectangleGhost(double[] pos) {
	  _ghost.setWidth(getWidth());
    _ghost.setHeight(getHeight());
    _ghost.setX(pos[0]);
    _ghost.setY(pos[1]);
    _ghostRotation.setPivotX(pos[0]);
    _ghostRotation.setPivotY(pos[1]);
    _ghostRotation.setAngle(_sectionData.getRotation());
  }

  public void checkShapeCollision() {
    if (Engine().isOtherShapeUnder(_shape, getShape()) == true)
      setContourColor(Color.RED);
    else
      setContourColor(Color.valueOf("#6378bf"));
  }
}