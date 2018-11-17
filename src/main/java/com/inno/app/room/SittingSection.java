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

import java.util.ArrayList;

public class SittingSection extends Section implements ImmutableSittingSection {

  private boolean _autoDistrib;
  private ArrayList<SittingRow> _rows = new ArrayList<SittingRow>();
  private VitalSpace _vitalSpace;

  public SittingSection(String idSection, double elevation, boolean autoDistrib, VitalSpace vitalSpace, double[] points) {
    super(idSection, elevation, points);
    this._autoDistrib = autoDistrib;
    //this._rows = ;
    this._vitalSpace = vitalSpace;
  }

  public void setAutoDistribution(boolean autoDistrib) {
    this._autoDistrib = autoDistrib;
  }

  public void deleteRow(String id) {
    //A FAIRE
  }

  public void createSeat(int idRow, double[] pos) {
    //A FAIRE
  }

  public void setVitalSpace(double height, double width) {
    this._vitalSpace.setHeight(height);
    this._vitalSpace.setWidth(width);
  }

  public ArrayList<SittingRow> getRows() {
    return this._rows;
  }

  /*public ImmutableSittingRow createRow() {
    //A FAIRE
  }*/

  public void addRow(String id) {
    //A FAIRE
  }

  public boolean isAutoDistribution() {
    return this._autoDistrib;
  }
  
  public ArrayList<? extends ImmutableSittingRow> getImmutableSittingRow() {
    return this._rows;
  }
  
  public VitalSpace getVitalSpace() {
    return this._vitalSpace;
  }
}