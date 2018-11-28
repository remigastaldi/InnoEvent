/*
 * File Created: Monday, 15th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
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
  private InnoRow[] _rows = null;

  private boolean _grabbed = false;

  public InnoRectangle(InnoEngine engine, Pane pane) {
    super(engine, pane);

    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth());
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight());
  }

  public InnoRectangle(InnoEngine engine, Pane pane, String id, double x, double y, double width, double height,
      Rotate rotation, Color color) {
    super(engine, pane, x, y, width, height, rotation, color);

    setID(id);
    _xVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth());
    _yVitalSpace = ((InnoEngine)Engine()).meterToPixel(Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight());
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
    if (getWidth() < ((InnoEngine)Engine()).meterToPixel(_xVitalSpace))
      setWidth(((InnoEngine)Engine()).meterToPixel(_xVitalSpace));
    if (getHeight() < ((InnoEngine)Engine()).meterToPixel(_yVitalSpace))
      setHeight(((InnoEngine)Engine()).meterToPixel(_yVitalSpace));

    if (!_grabbed) {
      // double[] pos = getPoints();
      // double[] newPos = new double[] { pos[0] - getWidth(), pos[1] - getHeight(), pos[2] - getWidth(),
      //     pos[3] - getHeight(), pos[4] - getWidth(), pos[5] - getHeight(), pos[6] - getWidth(), pos[7] - getHeight() };
      _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(localToParent(getPoints())), 0, true);
    } else
      _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, true);
    loadFromData();
    InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
    engine.getView().openPopup("new_sitting_rectangulary_section.fxml", this);
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  @Override
  public void onShapeChanged() {
    if (_sectionData == null)
      return;
    // double[] test = ((InnoEngine)Engine()).pixelToMeter(getPointsInParent());
    // for (int i = 0; i < test.length; i+=2) {
    //   System.out.println(test[i] + " " + test[i + 1 ]);
    // }
    // Core.get().updateSectionPositions(getID(), Engine().pixelToMeter(getPointsInParent()));
    // Core.get().setSectionRotation(getID(), getRotation() != null ? getRotation().getAngle() : 0.0);
    // loadFromData(_group);
  }

  @Override
  public boolean onMouseOnDragDetected(MouseEvent event) {
    _grabbed = true;
    return true;
  }

  @Override
  public boolean onFormComplete() {
    // if (getWidth() < ((InnoEngine)Engine()).meterToPixel(_xVitalSpace))
    // setWidth(((InnoEngine)Engine()).meterToPixel(_xVitalSpace));
    // if (getHeight() < ((InnoEngine)Engine()).meterToPixel(_yVitalSpace))
    // setHeight(((InnoEngine)Engine()).meterToPixel(_yVitalSpace));

    // double[] pos = {getX(), getY(),
    // getMaxXProperty().get(), getY(),
    // getMaxXProperty().get(), getMaxYProperty().get(),
    // getX(), getMaxYProperty().get() };
    // _sectionData = Core.get().createSittingSection(0, pos, 0);
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
  }

  public void setRowNumber(int rows) {
    setHeight(_yVitalSpace * rows);
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

    // System.outprintln()
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

  public void loadDomainData() {
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());
  }

  private void loadFromData(Group group) {
    setID(_sectionData.getIdSection());
    
    // double[] pos = getPoints();
    double[] pos = ((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions());
    
    // for (int i = 0; i < pos.length; i+=2) {
      //   System.out.println(pos[i] + " " + pos[i + 1 ]);
      // }

    if (group != null)
      setPoints(parentToLocal(pos));
    else
      setPoints(pos);

    ArrayList<? extends ImmutableSittingRow> rows =  _sectionData.getImmutableSittingRows();
    _rows = new InnoRow[rows.size()];
      
    int i = 0;
    getAdditionalShapes().clear();
    for (ImmutableSittingRow row : rows) {
      InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
      _rows[i] = new InnoRow(engine, this, row, engine.meterToPixel(_sectionData.getImmutableVitalSpace().getHeight()));
      ++i;
    }

    setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));
    // refreshGroup();
  }

  private void loadFromData() {
    loadFromData(null);
  }
}