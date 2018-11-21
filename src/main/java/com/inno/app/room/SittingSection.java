/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 20th November 2018
 * Modified By: GASTALDI Rémi
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

  public SittingSection(String idSection, double elevation, double[] points, double rotation, double vitalSpaceHeight, double vitalSpaceWidth) {
    super(idSection, elevation, points, rotation);
    this._autoDistrib = true;
    //this._rows = ;
    this._vitalSpace = new VitalSpace(vitalSpaceHeight, vitalSpaceWidth);
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

  public void setVitalSpace(double width, double height) {
    this._vitalSpace.setWidth(width);
    this._vitalSpace.setHeight(height);
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
  
  public ImmutableVitalSpace getImmutableVitalSpace() {
    return this._vitalSpace;
  }
}