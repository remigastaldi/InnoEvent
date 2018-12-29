/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.app;

import java.io.Serializable;

import com.inno.app.room.ImmutableRoom;
import com.inno.service.pricing.Pricing;

public class SaveObject implements Serializable {
  private static final long serialVersionUID = 1L;
  ImmutableRoom _roomData = null;
  Pricing _pricing = null;
  
  public SaveObject(ImmutableRoom roomData, Pricing pricing) {
    _roomData = roomData;
    _pricing = pricing;
  }

  public ImmutableRoom getRoomData() {
    return _roomData;
  }

  public Pricing getPricing() {
    return _pricing;
  }
}