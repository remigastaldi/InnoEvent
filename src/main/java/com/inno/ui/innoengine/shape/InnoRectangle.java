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

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;

public class InnoRectangle extends InteractiveRectangle {
  private double _xVitalSpace = 0.0;
  private double _yVitalSpace = 0.0;
  private ImmutableSittingSection _sectionData = null;
  private InnoRow[] _rows = null;
  private boolean _mousePressed = false;
  // private Rotate _rotation = null;

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
    // updateFromData();
    return true;
  }

  @Override
  public boolean onMouseReleased(MouseEvent event) {
    _mousePressed = false;

    if (getWidth() < ((InnoEngine)Engine()).meterToPixel(_xVitalSpace))
      setWidth(((InnoEngine)Engine()).meterToPixel(_xVitalSpace));
    if (getHeight() < ((InnoEngine)Engine()).meterToPixel(_yVitalSpace))
      setHeight(((InnoEngine)Engine()).meterToPixel(_yVitalSpace));

      // double[] tmp = getTest();
    // getRotation().setAngle(0d);
    _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getNoRotatedPos()), 0, true);
    setID(_sectionData.getIdSection());

    // setRotation(rotate);
    // double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    // setPoints((pos));
    // updateRowsFromData();
    // setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));
    updateFromData();

    InnoEngine engine = (InnoEngine) ((InnoEngine)Engine());
    engine.getView().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", this);
    return true;
  }

  public double[] getNoRotatedPos() {
    double[] rotated = getPointsInParent();
    double[] pos = new double[rotated.length];

    // All points by clock wise
    pos[0] = rotated[0];
    pos[1] = rotated[1];
    pos[2] = pos[0] + getWidth();
    pos[3] = pos[1];
    pos[4] = pos[2];
    pos[5] = pos[1] + getHeight();
    pos[6] = pos[0];
    pos[7] = pos[5];

    return pos;
  }

  @Override
  public boolean onShapeMoved() {
    if (_sectionData == null)
      return true;
    // double rotation = getRotation().getAngle();
    double[] pos = getNoRotatedPos();
    for (int i = 0; i < pos.length; i+=2) {
      System.out.println("TEST --> X " + pos[i] + " Y " + pos[i + 1]);
    }

    // getRotation().setAngle(0d);
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getNoRotatedPos()), true);


    double[] parent = getPointsInParent();
    for (int i = 0; i < parent.length; i+=2) {
      System.out.println("PARENT --> X " + parent[i] + " Y " + parent[i + 1]);
    }

    double[] local = getPoints();
    for (int i = 0; i < local.length; i+=2) {
      System.out.println("LOCAL --> X " + local[i] + " Y " + local[i + 1]);
    }

    updatePositionFromData();
    return true;
  }

  @Override
  public boolean onAnchorDragged() {
    if (_sectionData == null)
      return true;

      // System.out.println("-->> " + getWidth() + " " + getColumnNumber());
      // setColumnNumber(getColumnNumber());
      // System.out.println("################### RESIZED ######################");
      // _rotation = getRotation();s
      // getGroup().getTransforms().clear();
    double rotation = getRotation().getAngle();
    getRotation().setAngle(0d);
    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);
    // updateRowsFromData();
    getRotation().setAngle(rotation);

    // double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));    
    // _rotation.setAngle(_sectionData.getRotation());
    // _rotation.setPivotX(pos[0]);
    // _rotation.setPivotY(pos[1]);
    // // _rotation = 
    // setRotation(_rotation);
    // getGroup().setRotationAxis(new Point3D(_rotation.getPivotX(), _rotation.getPivotY(), 0));
    // updatePositionFromData();
    // System.out.println(getPointsInParent()[0] +  " " + getPointsInParent()[1]);
    // updateFromData();
    return true;
  }

  // public double[] getTest() {
  //   double[] pointsInParent = getPointsInParent();
  //   double[] newP = new double[pointsInParent.length];

  //   System.out.println("ROTATION =====> " + getRotation().getAngle());
  //   for (int i = 0; i < pointsInParent.length; i+=2) {
  //     Point2D point =  getRotation().inverseTransform(pointsInParent[ i], pointsInParent[i + 1]);
  //     newP[i] = point.getX();
  //     newP[i + 1] = point.getY();
  //     System.out.println("||| " + newP[i] + " " +  newP[i + 1]);
  //   }

  //   System.out.println();

  //   return newP;
  // }

  @Override
  public boolean onAnchorReleased() {
    System.out.println("REALEASED");

    double[] parent = getPointsInParent();
    
    getRotation().setAngle(0);
    double [] pos = parentToLocal(parent);
 
    getGroup().getTransforms().clear();
    setPoints(pos);

    Core.get().updateSectionPositions(getID(), ((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), true);

    setPoints(pos);
    updateRowsFromData();
    // getRotation().setPivotX(pos[0]);
    // getRotation().setPivotY(pos[1]);
    // getRotation().setAngle(_sectionData.getRotation());
    // updatePositionFromData();
    setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));
    return true;
  }

  @Override
  public boolean onFormComplete() {
    if (_mousePressed == true) {

      // double[] pos = getPointsInParent();
      // for (int i = 0; i < pos.length; i += 2) {
      //   System.out.println("X: " + pos[i] + " Y: " + pos[i + 1]);
      // } 

      // _sectionData = Core.get().createSittingSection(((InnoEngine)Engine()).pixelToMeter(getPointsInParent()), 0, true);
      // setID(_sectionData.getIdSection());
      // updateFromData();
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

  public void loadFromData() {
    // System.out.println("################### CREATED ######################");
    _sectionData = Core.get().getImmutableRoom().getImmutableSittingSections().get(getID());

    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));

    // _rotation = new Rotate();
    
    closeForm(pos[0], pos[1], new Rotate(), Color.valueOf(Core.get().getSectionPrice(getID()).getColor()));
    
    // _group.setOnMousePressed(event -> {
    //   System.out.println("=============");
    //   _mousePressed = true;
    // });
    // _group.setOnMouseReleased(event -> {
    //   _mousePressed = false;
    // });

    updateFromData();
  } 

  private void updateFromData() {
    // getRotation().setAngle(0);
    updateRowsFromData();
    updatePositionFromData();
  }

  private void updatePositionFromData() {
    double[] pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    getRotation().setAngle(0);

    setPoints(pos);
    getRotation().setPivotX(pos[0]);
    getRotation().setPivotY(pos[1]);
    getRotation().setAngle(_sectionData.getRotation());

    // setRotation(new Rotate(_sectionData.getRotation(), pos[0], pos[1]));

    // double[] pos = null;

    // // if (fromParent) {
    //   pos = parentToLocal(((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions()));
    //   double []pos2 = ((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions());
    // // } else {
    //   // System.out.println("-----------------------------------");
    //   // }
      
    //   // pos = ((InnoEngine)Engine()).meterToPixel(_sectionData.getPositions());
    //   System.out.println(" : " + pos[0] + " " + pos[1]);
    //   System.out.println(" : " + pos2[0] + " " + pos2[1]);
    //   // setPoints((pos));
    //   // _rotation.setPivotX(pos[0]);
    //   // _rotation.setPivotY(pos[1]);
    //   // getRotation().setAngle(_sectionData.getRotation());

    //   // pos = getPoints();
    //   Circle circle =  new Circle(pos2[0], pos2[1], 3, Color.ORANGE);
    //   Pane().getChildren().add(circle);
    //   pos = getPointsInParent();
    //   circle =  new Circle(pos[0], pos[1], 3, Color.PINK);
    //   Pane().getChildren().add(circle);
    }

  private void updateRowsFromData() {
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
      _rows[i] = new InnoRow(engine, this, _sectionData, row);
      ++i;
    }
  }
}