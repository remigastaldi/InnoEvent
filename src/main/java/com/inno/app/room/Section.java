/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 10th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.io.Serializable;

public class Section implements ImmutableSection, Serializable {

  private static final long serialVersionUID = 1L;
  private String _nameSection;
  private String _idSection;
  private double _elevation;
  private double[] _positions; //[3...*]
  private double _rotation;
  private double _userRotation = 0d;

  public Section(String nameSection, String idSection, double[] positions, double rotation) {
    this._nameSection = nameSection;
    this._idSection = idSection;
    this._elevation = 0;
    this._positions = positions;
    this._rotation = rotation;
  }

  public void setNameSection(String nameSection) {
    this._nameSection = nameSection;
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

  public String getNameSection() {
    return this._nameSection;
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

  public void setUserRotation(double rotation) {
    _userRotation = rotation;
  }

  public double getUserRotation() {
    return _userRotation;
  }
}