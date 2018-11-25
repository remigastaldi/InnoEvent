/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public class Seat implements ImmutableSeat {

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