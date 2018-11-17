/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Friday, 16th November 2018
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

  public Section(String idSection, double elevation, double[] positions) {
    this._idSection = idSection;
    this._elevation = elevation;
    this._positions = positions;
  }

  public void setIdSection(String idSection) {
    this._idSection = idSection;
  }

  public void setElevation(double elevation) {
    this._elevation = elevation;
  }

  public void updatePosition(double[] positions) {
    this._positions = positions;
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
}