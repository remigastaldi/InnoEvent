/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 14th December 2018
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
import com.inno.app.room.Section;
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
    if (_sittingSectionData == null)
      return true;

    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), false);
    updatePositionFromData();
    
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (mouseReleased) {
      System.out.println("FORME COLMPLETE");
      _sittingSectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, false);
      setID(_sittingSectionData.getIdSection());

      Point2D center = Engine().getCenterOfPoints(getPoints());
      setRotation(new Rotate(_sittingSectionData.getRotation(), center.getX(), center.getY()));
  
      updateFromData();
      select();
    }
    return true;
  }

  @Override
  public boolean onDestroy() {
    Core.get().deleteSection(getID());
    return true;
  }

  @Override
  public boolean onAnchorDragged() {
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), false);
    updatePositionFromData();
    updateRowsFromData(true);
    
    return true;
  }

  @Override
  public boolean onAnchorReleased() {
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedParentPos()), false);
    updateFromData();
    // double[] parent = getPointsInParent();
    
    // getRotation().setAngle(0);
    // double [] pos = parentToLocal(parent);
 
    // getGroup().getTransforms().clear();
    // setPoints(pos);

    // Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);

    // setPoints(pos);
    // double rotation = _sittingSectionData != null ? _sittingSectionData.getRotation() : 0;
    // setRotation(new Rotate(rotation, pos[0], pos[1]));
    // updateRowsFromData(false);
 
    return true;
  }

  @Override
  public boolean onSelected() {
    if (getID() == null)
      return false;
    InnoEngine engine = (InnoEngine) Engine();
    if (_sittingSectionData != null)
      engine.getView().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml", this);
    else
      engine.getView().setSidebarFromFxmlFileName("sidebar_standing_section.fxml", this);
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
      updateFromData();
    }

  private void updateFromData() {
    updatePositionFromData();
    updateRowsFromData(false);
  }

  private void updatePositionFromData() {
      setPoints(parentToLocal(((InnoEngine)Engine()).meterToPixel(_sittingSectionData.getPositions())));
  }

  // TODO: private this
  public void updateRowsFromData(boolean toParent) {
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
    _standingSectionData = Core.get().sittingToStandingSection(_sittingSectionData.getIdSection());
    _sittingSectionData = null;
    
    destroyRows();
  }

  public void standingToSitting() {
    _sittingSectionData = Core.get().standingToSittingSection(_standingSectionData.getIdSection());
    _standingSectionData = null;
    updateFromData();
  }

  public ImmutableSittingSection getSittingData() {
    return _sittingSectionData;
  }

  public ImmutableStandingSection getStandingData() {
    return _standingSectionData;
  }
}