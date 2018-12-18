/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Friday, 14th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.io.Serializable;

public class Seat implements ImmutableSeat, Serializable {
  private static final long serialVersionUID = 1L;
  private int _idSeat;
  private double[] _pos = new double[2];

  public Seat(int id, double[] pos) {
    this._idSeat = id;
    this._pos = pos;
  }

  public void setPosition(double[] pos) {
    this._pos = pos;
  }

  public int getId() {
    return this._idSeat;
  }
  
  public double[] getPosition() {
    return this._pos;
  }
}