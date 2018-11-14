/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 13th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.pricing;

public class PlaceRate extends PlaceRateData {

  public PlaceRate(String id, String color, double price) {
    super(id, color, price);
  }

  public void setPrice(double price) {
    this._price = price;
  } 

  public void setColor(String color) {
    this._color = color;
  }

  public boolean addOffer(String offerName) {
    return this._offers.add(offerName);
  }

  public boolean removeOffer(String offerName) {
    return this._offers.remove(offerName);
  }
};