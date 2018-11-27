/*
 * File Created: Thursday, 8th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.io.Serializable;

public class Scene implements ImmutableScene, Serializable {

  private static final long serialVersionUID = 1L;
  private double _width;
  private double _height;
  private double[] _positions = new double[8];
  private double _rotation;
  
  public Scene(double width, double height, double[] positions) {
    this._width = width;
    this._height = height;
    this._positions = positions;
    for (int i = 0; i < _positions.length; i+=2) {
      System.out.println(";;; " + _positions[i] + " : " + _positions[i + 1]);
    }

    this._rotation = 0;
  }

  public void setWidth(double width) {
    this._width = width;
  }

  public void setHeight(double height) {
    this._height = height;
  }

  public void setPositions(double[] positions) {
    this._positions = positions;
  }

  public void setRotation(double rotation) {
    this._rotation = rotation;
  }

  public double getWidth() {
    return this._width;
  }
  
  public double getHeight() {
    return this._height;
  }

  public double[] getPositions() {
    return this._positions;
  }

  public double getRotation() {
    return this._rotation;
  }

  public double[] getCenter() {
    return new double[]{_positions[0] + Math.hypot(_positions[0] - _positions[2], _positions[1] - _positions[3]) / 2,
        _positions[1] + Math.hypot(_positions[6] - _positions[0], _positions[7] - _positions[1]) / 2};
  }
}