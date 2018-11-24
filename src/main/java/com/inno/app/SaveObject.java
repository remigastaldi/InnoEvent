/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.app;

import java.io.Serializable;

import com.inno.app.room.ImmutableRoom;

public class SaveObject implements Serializable {
  private static final long serialVersionUID = 1L;
  ImmutableRoom _roomData = null;
  //TODO: Add Prices
  public SaveObject(ImmutableRoom roomData) {
    _roomData = roomData;
  }

  public ImmutableRoom getRoomData() {
    return _roomData;
  }
}