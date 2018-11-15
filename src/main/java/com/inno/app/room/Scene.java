/*
 * File Created: Thursday, 8th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public class Scene implements ImmutableScene {

  private double _width;
  private double _height;
  private double[] _positions = new double[8];
  
  public Scene(double width, double height, double[] positions) {
    this._width = width;
    this._height = height;
    this._positions = positions;
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

  public double getWidth() {
    return this._width;
  }
  
  public double getHeight() {
    return this._height;
  }

  public double[] getPositions() {
    return this._positions;
  }
}