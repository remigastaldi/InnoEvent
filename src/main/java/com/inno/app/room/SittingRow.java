/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.io.Serializable;
import java.util.ArrayList;

public class SittingRow implements Serializable, ImmutableSittingRow {

  private static final long serialVersionUID = 1L;
  private String _idRow = null;
  private double[] _posStart;
  private double[] _posEnd;
  private ArrayList<Seat> _seats = new ArrayList<Seat>();

  public SittingRow(String idRow, double[] posStart, double[] posEnd) {
    this._idRow = idRow;
    this._posStart = posStart;
    this._posEnd = posEnd;
  }

  public ImmutableSeat createSeat(double[] pos) {
    int id = this._seats.size();
    Seat seat = new Seat(id, pos);
    this._seats.add(seat);
    return seat;
  }

  public ArrayList<Seat> getSeats() {
    return this._seats;
  }

  public ArrayList<? extends ImmutableSeat> getImmutableSeats() {
    return this._seats;
  }

  public String getIdRow() {
    return this._idRow;
  }

  public double[] getPosStartRow() {
    return this._posStart;
  }

  public double[] getPosEndRow() {
    return this._posEnd;
  }
}