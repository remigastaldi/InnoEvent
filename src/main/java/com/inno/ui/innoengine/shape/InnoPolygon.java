/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 10th December 2018
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

  public InnoPolygon(InnoEngine engine, Pane pane, String id) {
    super(engine, pane);

    setID(id);

    loadFromData();
    // deselect();
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
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sittingSectionData.getPositions()));
    setPoints(pos);
    setRotation(new Rotate(_sittingSectionData.getRotation(), pos[0], pos[1]));
  
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
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), false);
    updateFromData();
    
    return true;
  }

  @Override
  public boolean onSelected() {
    if (getID() == null)
      return false;
    InnoEngine engine = (InnoEngine) Engine();
    engine.getView().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml", this);
    return true;
  }

  public void loadFromData() {
    _sittingSectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());

    double[] pos = ((InnoEngine)Engine()).meterToPixel(parentToLocal(_sittingSectionData.getPositions()));
    closeForm(pos, Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));

// setColor(Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));

    Point2D center = Engine().getCenterOfPoints(getPoints());
    setRotation(new Rotate(_sittingSectionData.getRotation(), center.getX(), center.getY()));

    updateFromData();
  }

  private void updateFromData() {
    setPoints(parentToLocal(((InnoEngine)Engine()).meterToPixel(_sittingSectionData.getPositions())));

    // Point2D center = Engine().getCenterOfPoints(getPoints());
    // setRotation(new Rotate(_sittingSectionData.getRotation(),center.getX(), center.getY()));

    if (_rows != null) {
      for (int i = 0; i < _rows.length; ++i) {
        _rows[i].destroy();
      }
    }

    ArrayList<? extends ImmutableSittingRow> rows =  _sittingSectionData.getImmutableSittingRows();
    _rows = new InnoRow[rows.size()];
    
    
    int i = 0;
    for (ImmutableSittingRow row : rows) {
      InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
      _rows[i] = new InnoRow(engine, this, _sittingSectionData, row, false);
      ++i;
    }
  }

  public void changeSectionType() {
    //TODO Sitting to standing anv vis versa
  }
}