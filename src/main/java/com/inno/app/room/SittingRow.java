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

import java.util.ArrayList;

public class SittingRow implements ImmutableSittingRow {

  protected String _idRow;
  protected ArrayList<Seat> _seats = new ArrayList<Seat>();

  public SittingRow(String idRow) {
    this._idRow = idRow;
  }

  public ArrayList<Seat> getSeats() {
    return this._seats;
  }

  public ImmutableSeat createSeat(double[] pos) {
    int id = 0;
    ImmutableSeat seatData = new Seat(id, pos);
    //A FAIRE
    return seatData;
  }

  public ArrayList<? extends ImmutableSeat> getImmutableSeats() {
    return this._seats;
}
}