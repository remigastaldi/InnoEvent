/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 20th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public class Section implements ImmutableSection {

  private String _idSection;
  private double _elevation;
  private double[] _positions; //[3...*]
  private double _rotation;

  public Section(String idSection, double elevation, double[] positions, double rotation) {
    this._idSection = idSection;
    this._elevation = elevation;
    this._positions = positions;
    this._rotation = rotation;
  }

  public void setIdSection(String idSection) {
    this._idSection = idSection;
    System.out.println(this._idSection);
  }

  public void setElevation(double elevation) {
    this._elevation = elevation;
  }

  public void updatePosition(double[] positions) {
    this._positions = positions;
  }

  public void setRotation(double rotation) {
    this._rotation = rotation;
  }

  public String getIdSection() {
    return this._idSection;
  }

  public double getElevation() {
    return this._elevation;
  }

  public double[] getPositions() {
    return this._positions;
  }

  public double getRotation() {
    return this._rotation;
  }

}