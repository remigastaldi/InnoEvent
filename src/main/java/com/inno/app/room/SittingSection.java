/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Thursday, 29th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.util.ArrayList;
import java.util.Iterator;

public class SittingSection extends Section implements ImmutableSection, ImmutableSittingSection {

  private static final long serialVersionUID = 1L;
  private boolean _autoDistrib;
  private ArrayList<SittingRow> _rows = new ArrayList<SittingRow>();
  private VitalSpace _vitalSpace;
  private boolean _isRectangle = false;

  public SittingSection(String nameSection, String idSection, double[] points, double rotation, double vitalSpaceHeight, double vitalSpaceWidth, boolean isRectangle) {
    super(nameSection, idSection, points, rotation);
    this._autoDistrib = true;
    this._vitalSpace = new VitalSpace(vitalSpaceHeight, vitalSpaceWidth);
    _isRectangle = isRectangle;
  }

  public void setAutoDistribution(boolean autoDistrib) {
    this._autoDistrib = autoDistrib;
  }

  public void setVitalSpace(double width, double height) {
    this._vitalSpace.setWidth(width);
    this._vitalSpace.setHeight(height);
  }

  public static String toBase26(int number) {
    String convert = "";
    char letter = 'A';
    int i = 1;

    for (i = 1; i <= number; ++i) {
      if (i % 26 == 0) {
        if (number - i < 26)
          convert += letter;
        letter++;
        if (letter == 91)
          letter = 'A';
      }
    }
    letter = (char) ((number - (26 * (letter - 'A'))) + 'A');
    convert += letter;
    return convert;
  }

  public ImmutableSittingRow createRow(double[] posStart, double[] posEnd) {
    String id = toBase26(this._rows.size());
    SittingRow row = new SittingRow(id, posStart, posEnd);
    this._rows.add(row);
    return row;
  }

  public void deleteRow(String idRow) {
    Iterator<SittingRow> i = this._rows.iterator();
    while (i.hasNext()) {
      if (i.next().getIdRow().compareTo(idRow) == 0) {
        i.remove();
        break;
      }
    }
  }

  public void clearAllRows() {
    this._rows.clear();
  }

  public ImmutableSeat createSeat(String idRow, double[] pos) {
    Iterator<SittingRow> i = this._rows.iterator();
    ImmutableSeat seat = null;
    while (i.hasNext()) {
      SittingRow row = i.next();
      if (row.getIdRow().compareTo(idRow) == 0) {
        seat = row.createSeat(pos);
      }
    }
    return seat;
  }

  public boolean getAutoDistribution() {
    return this._autoDistrib;
  }
  
  public ArrayList<SittingRow> getRows() {
    return this._rows;
  }

  public ArrayList<? extends ImmutableSittingRow> getImmutableSittingRows() {
    return this._rows;
  }
  
  public ImmutableVitalSpace getImmutableVitalSpace() {
    return this._vitalSpace;
  }

  @Override
  public boolean isRectangle() {
    return _isRectangle;
  }
}