/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public class Section implements ImmutableSection {

  private String _name;
  private double _elevation;
  private int _idSection;
  private double[] _points; //[3...*]

  public Section(String name, double elevation, int idSection, double[] points) {
    this._name = name;
    this._elevation = elevation;
    this._idSection = idSection;
    this._points = points;
  }

  public void setIdSection() {
    //A FAIRE
  }

  public void setName(String name) {
    this._name = name;
  }

  public void setElevation(double elevation) {
    this._elevation = elevation;
  }

  public void updatePosition(double[] points) {
    this._points = points;
  }

  public String getName() {
    return this._name;
  }

  public double getElevation() {
    return this._elevation;
  }

  public double[] getPoints() {
    return this._points;
  }
}