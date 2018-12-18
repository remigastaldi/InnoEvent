/*
 * File Created: Sunday, 14th October 2018
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
import com.inno.app.room.ImmutableSection;
import com.inno.app.room.ImmutableSittingRow;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.ImmutableStandingSection;
import com.inno.ui.engine.shape.InteractivePolygon;
import com.inno.ui.innoengine.InnoEngine;
import com.inno.ui.innoengine.InnoRow;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class InnoPolygon extends InteractivePolygon {
  private ImmutableStandingSection _standingSectionData = null;
  private ImmutableSittingSection _sittingSectionData = null;
  private boolean mouseReleased = false;
  private InnoRow[] _rows = null;

  public InnoPolygon(InnoEngine engine, Pane pane) {
    super(engine, pane);
  }

  public InnoPolygon(InnoEngine engine, Pane pane, String id, double[] pos, Rotate rotation, Color color) {
    super(engine, pane, pos, rotation, color);

    setID(id);
  }

  public InnoPolygon(InnoEngine engine, Pane pane, String id, boolean isStanding) {
    super(engine, pane);

    setID(id);

    loadFromData(isStanding);
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
  public boolean onShapeMoved() {
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), false);
    updateFromData(true);
    
    return true;
  }

  @Override
  public boolean onGroupReleased() {
    updateFromData(false);
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (mouseReleased) {
      _sittingSectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, false);
      setID(_sittingSectionData.getId());

      Point2D center = Engine().getCenterOfPoints(getPoints());
      setRotation(new Rotate(_sittingSectionData.getRotation(), center.getX(), center.getY()));

      updateFromData(false);
      select();

      ((InnoEngine)Engine()).addPolygon(this);
    }
    return true;
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    ((InnoEngine)Engine()).deleteRectangle(getID());
    return true;
  }

  @Override
  public boolean onAnchorDragged() {
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), false);
    updateFromData(false);
    
    return true;
  }

  @Override
  public boolean onAnchorReleased() {
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), false);
    updateFromData(false);
 
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
    InnoEngine engine = (InnoEngine) Engine();
    if (_sittingSectionData != null)
      engine.getView().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml", this);
    else
      engine.getView().setSidebarFromFxmlFileName("sidebar_standing_section.fxml", this);
    _shape.toBack();
    return true;
  }

  public void loadFromData(boolean isStanding) {
    ImmutableSection section = null;

    if (isStanding) {
      section = Core.get().getImmutableRoom().getImmutableStandingSections().get(getID());
      _standingSectionData = (ImmutableStandingSection) section;
    }
    else {
      section = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());
      _sittingSectionData = (ImmutableSittingSection) section;
    }
    
    double[] pos = ((InnoEngine)Engine()).meterToPixel(parentToLocal(section.getPositions()));
    closeForm(pos, Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));
    
    Point2D center = Engine().getCenterOfPoints(getPoints());
    setRotation(new Rotate(section.getRotation(), center.getX(), center.getY()));

    if (!isStanding)
      updateFromData(false);
  }

  public void updateFromData(boolean toParent) {
    getRotation().setAngle(0);
    // updateRowsFromData(toParent);
    updateRowsFromData(false);
    updatePositionsFromData();
    checkShapeCollision();
  }

  private void updatePositionsFromData() {
    ImmutableSection section = _sittingSectionData != null ? _sittingSectionData : _standingSectionData;
    
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(section.getPositions()));

    updatePoints(pos);
    Rotate rotation = getRotation();
    Point2D center = Engine().getCenterOfPoints(pos);
    rotation.setPivotX(center.getX());
    rotation.setPivotY(center.getY());
    rotation.setAngle(section.getRotation());
  }

  public void updateRowsFromData(boolean toParent) {
    if (_sittingSectionData == null)
      return;

    destroyRows();
    
    ArrayList<? extends ImmutableSittingRow> rows =  _sittingSectionData.getImmutableSittingRows();
    _rows = new InnoRow[rows.size()];

    int i = 0;
    for (ImmutableSittingRow row : rows) {
      InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
      _rows[i] = new InnoRow(engine, this, _sittingSectionData, row, toParent);
      ++i;
    }
  }

  public void destroyRows() {
    if (_rows != null) {
      for (int i = 0; i < _rows.length; ++i) {
        _rows[i].destroy();
      }
    }
  }

  public void sittingToStanding() {
    _standingSectionData = Core.get().sittingToStandingSection(_sittingSectionData.getId());
    _sittingSectionData = null;
    destroyRows();
    setID(_standingSectionData.getId());
    ((InnoEngine) Engine()).getView().setSidebarFromFxmlFileName("sidebar_standing_section.fxml", this);
  }

  public void standingToSitting() {
    InnoEngine engine = (InnoEngine) Engine();
    _sittingSectionData = Core.get().standingToSittingSection(_standingSectionData.getId());
    _standingSectionData = null;
    setID(_sittingSectionData.getId());
    updateFromData(false);
    // updateRowsFromData(false);
    setColor(Color.valueOf("#6378bf").deriveColor(1, 1, 0.8, 0.85));
    engine.getView().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml", this);
  }

  public void setVitalSpace(double width, double height) {
    Core.get().setSittingSectionVitalSpace(_sittingSectionData.getId(), width, height);
  }

  public ImmutableSittingSection getSittingData() {
    return _sittingSectionData;
  }

  public ImmutableStandingSection getStandingData() {
    return _standingSectionData;
  }

  public void checkShapeCollision() {
    if (Engine().isOtherShapeUnder(_shape) == true)
      setContourColor(Color.RED);
    else
      setContourColor(Color.valueOf("#6378bf"));
  }
}